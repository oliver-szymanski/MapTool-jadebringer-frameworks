package de.jadebringer.maptool.extensionframework;

import java.awt.EventQueue;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.rptools.lib.AppEvent;
import net.rptools.lib.AppEventListener;
import net.rptools.maptool.client.AppState;
import net.rptools.maptool.client.MapTool;
import net.rptools.maptool.client.MapToolVariableResolver;
import net.rptools.maptool.client.MapTool.ZoneEvent;
import net.rptools.maptool.client.functions.AbortFunction.AbortFunctionException;
import net.rptools.maptool.client.ui.zone.ZoneRenderer;
import net.rptools.maptool.model.AbstractPoint;
import net.rptools.maptool.model.CellPoint;
import net.rptools.maptool.model.InitiativeList;
import net.rptools.maptool.model.InitiativeList.TokenInitiative;
import net.rptools.maptool.model.ModelChangeEvent;
import net.rptools.maptool.model.ModelChangeListener;
import net.rptools.maptool.model.Path;
import net.rptools.maptool.model.Player;
import net.rptools.maptool.model.Token;
import net.rptools.maptool.model.Zone;
import net.rptools.maptool.model.ZonePoint;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.CycleDetectionStrategy;
import net.sf.json.util.PropertyFilter;

/** 
 * Capture the spread events in any possible way and 
 * distribute it further.
 * 
 * @author naciron
 *
 */
public class EventDispatcher implements AppEventListener, PropertyChangeListener, ModelChangeListener, ContainerListener {

  public static enum EventType {
    ApplicationEvent,
    CampaignEvent,
    InitiativeEvent,
    TokenEvent
  }
  public static enum EventSubType {
    ApplicationStarting,
    CampaignLoaded,
    CampaignNew,
    InitiativeCurrentChanged,
    InitiativeRoundChanged,
    InitiativeTokenAdded,
    InitiativeTokenRemoved,
    InitiativeSorted,
    TokenMoved
  }
  private static final EventDispatcher instance = new EventDispatcher();
  private static final String ON_HANDLE_EVENT_CALLBACK = "onHandleEvent";
  private int initializingCampaignState = -1;
  private Set<EventHandler> eventHandlers = new HashSet<>();
  protected EventDispatcher() {
  }
  
  public static EventDispatcher getInstance() {
    return instance;
  }
  
  void init() {
    // need to get Zone events
    MapTool.getEventDispatcher().addListener(this, ZoneEvent.values());
    // needed to get Campaign events by checking if campaign panel changed
    MapTool.getFrame().getCampaignPanel().addContainerListener(this);
    // we get initiave events by subscribing to zones anytime a zone changes during that events
    registerThisToZone();
  }
  
  // METHOD FOR DISPATCHING ALL GATHERED EVENTS
  // ------------------------------------------
  
  public void addEventHandler(EventHandler eventHandler) {
    this.eventHandlers.add(eventHandler);
  }
  
  public void removeEventHandler(EventHandler eventHandler) {
    this.eventHandlers.remove(eventHandler);
  }

  protected boolean raiseVetoableEvent(Event event, boolean macroCallback) {
    for(EventHandler eventHandler : this.eventHandlers) {
      boolean result = eventHandler.handleVetoableEvent(event);
      if (!result) {
        logEvent(event);
        return false;
      }
    }
    
    logEvent(event);
    if (macroCallback)
      callCallbackMacro(event);
    return true;
  }
  
  protected void raiseEvent(Event event, boolean macroCallback) {
    // try to register later
    EventQueue.invokeLater(
        new Runnable() {
          public void run() {
            for(EventHandler eventHandler : eventHandlers) {
              eventHandler.handleEvent(event);
            }
            
            logEvent(event);
            if (macroCallback)
              callCallbackMacro(event);
          }
        });
  }

  static ZoneRenderer getCurrentZoneRenderer() {
    return MapTool.getFrame().getCurrentZoneRenderer();
  }

  private void callCallbackMacro(Event event) {
    List<ZoneRenderer> zrenderers = MapTool.getFrame().getZoneRenderers();
    for (ZoneRenderer zr : zrenderers) {
      List<Token> tokenList =
          zr.getZone()
              .getTokensFiltered(
                  new Zone.Filter() {
                    public boolean matchToken(Token t) {
                      return t.getName().toLowerCase().startsWith("lib:");
                    }
                  });

      for (Token token : tokenList) {
        // If the token is not owned by everyone and all owners are GMs
        // then we are in
        // its a trusted Lib:token so we can run the macro
        if (token != null) {
          if (token.isOwnedByAll()) {
            continue;
          } else {
            Set<String> gmPlayers = new HashSet<String>();
            for (Object o : MapTool.getPlayerList()) {
              Player p = (Player) o;
              if (p.isGM()) {
                gmPlayers.add(p.getName());
              }
            }
            for (String owner : token.getOwners()) {
              if (!gmPlayers.contains(owner)) {
                continue;
              }
            }
          }
        }
        // If we get here it is trusted so try to execute it.
        if (token.getMacro(ON_HANDLE_EVENT_CALLBACK, false) != null) {
          try {
            JsonConfig config = new JsonConfig();
            config.setCycleDetectionStrategy(CycleDetectionStrategy.NOPROP);
            PropertyFilter pf = new PropertyFilter(){  
              public boolean apply( Object source, String name, Object value ) {  
                 return value == null;  
              }  
            };
            config.setJsonPropertyFilter(pf);
           
            MapTool.getParser()
                .runMacro(
                    new MapToolVariableResolver(token),
                    token,
                    ON_HANDLE_EVENT_CALLBACK + "@" + token.getName(),
                    JSONObject.fromObject(event, config).toString());
          } catch (AbortFunctionException afe) {
            // Do nothing
          } catch (Exception e) {
            e.printStackTrace();
            MapTool.addLocalMessage(
                "Error running "
                    + ON_HANDLE_EVENT_CALLBACK
                    + " on "
                    + token.getName()
                    + " : "
                    + e.getMessage());
          }
        }
      }
    }
  }
  
 // METHOD FOR GATHERING EVENTS
 // ---------------------------

  @Override
  public void handleAppEvent(AppEvent event) {
    // This is called by MapToolFrame when a zone changes
    
    String eventType = event.getId().getClass().getSimpleName();
    String eventSubType = event.getId().name();
    String eventSource = event.getSource().getClass().getSimpleName();
    String eventTarget = null;    
    Object eventOldValue = event.getOldValue();
    Object eventValue = event.getNewValue();
    String additionalInfo = null;
    Event outEvent = null;
    
    if ("ZoneEvent".equalsIgnoreCase(eventType) && "Activated".equalsIgnoreCase(eventSubType)) {
      registerThisToZone();
      registerThisToInitiativeList(); // this has to be renewed every time the list changes

      outEvent = Event.create()
      .type(eventType).subType(eventSubType)
      .source("Zones").target(eventTarget).oldValue(eventOldValue).value(((Zone)eventValue).getName()).additionalInfo(additionalInfo);
      raiseEvent(outEvent, true);
    } else {
      // log unhandled event
      logEvent(eventType, eventSubType, eventSource, eventTarget, eventOldValue, eventValue, additionalInfo);
    }
  }

  @Override
  public void propertyChange(PropertyChangeEvent event) {
    // called when initiative list changes a value
    //   - that only happens once than a new list is create
    //   - so we register ourself in model change to each new list
    
    // set general event properties
    String eventType = "PropertyEvent";
    String eventSubType = "Change";
    Object eventSource = event.getSource().getClass().getSimpleName();
    Object eventTarget = event.getPropertyName();
    Object eventOldValue = event.getOldValue();
    Object eventValue = event.getNewValue();
    String additionalInfo = null;
    Event outEvent = null;

    // check for specific events
    
    // check initiative events
    if (event.getSource() == MapTool.getFrame().getCurrentZoneRenderer().getZone().getInitiativeList()) {
      outEvent = Event.create()
      .type(EventType.InitiativeEvent.name())
      .source(eventSource).target(eventTarget).oldValue(eventOldValue).value(eventValue).additionalInfo(additionalInfo);

      if ("current".equalsIgnoreCase(event.getPropertyName())) {
        outEvent.subType(EventSubType.InitiativeCurrentChanged.name());
        raiseEvent(outEvent, true);
      } else if("round".equalsIgnoreCase(event.getPropertyName())) {
        outEvent.subType(EventSubType.InitiativeRoundChanged.name());
        raiseEvent(outEvent, true);
      } else if ("tokens".equalsIgnoreCase(event.getPropertyName())) {
        InitiativeList initiativeList = MapTool.getFrame().getCurrentZoneRenderer().getZone().getInitiativeList();
        if (initiativeList.getTokens().contains(eventValue)) {
          // token was added to initiative list
          outEvent.subType(EventSubType.InitiativeTokenAdded.name()); 
          outEvent.value(((TokenInitiative)eventValue).getToken().getId().toString());
          raiseEvent(outEvent, true);
        } else if (eventOldValue == null && eventValue instanceof List) {
          // initiative list was sorted
          outEvent.subType(EventSubType.InitiativeSorted.name());
          List<String> ids = new LinkedList<String>();
          @SuppressWarnings("unchecked")
          List<TokenInitiative> tokens = (List<TokenInitiative>)eventValue;
          for(TokenInitiative token : tokens) {
            ids.add(token.getId().toString());
          }
          outEvent.value(ids);
          raiseEvent(outEvent, true);
        } else {
          outEvent.subType(EventSubType.InitiativeTokenRemoved.name());          
          if (eventOldValue instanceof List) {
            // tokens were removed from initiative list
            for(Object item : ((List<?>)eventOldValue)) {
              outEvent.oldValue(((TokenInitiative)item).getToken().getId().toString());
              outEvent.value(null);
              raiseEvent(outEvent, true);
            }
          } else {
            // token was removed from intiative lsit
            outEvent.oldValue(((TokenInitiative)eventOldValue).getToken().getId().toString());
            outEvent.value(null);
            raiseEvent(outEvent, true);
          }
        }
      } else {
        outEvent = null;
      }
    } else {
      // log not handled event
      logEvent(eventType, eventSubType, eventSource, eventTarget, eventOldValue, eventValue, additionalInfo);
    }
  }
  
  public void onTokensMoved(List<Token> tokens) {
    // used from the DelegateZoneRenderer work around to get 
    
    for(Token token : tokens) {
      if (token.getLastPath() != null) {
        // token was not denied as the last path is != null
        
        // get last path in grid/non grid (depends on if the map is in grid)
        List<Map<String, Integer>> pathPoints = getLastPathList(token.getLastPath(), false);
        JSONArray pathArr = pathPointsToJSONArray(pathPoints);
        String pathCoordinates = pathArr.toString();
        
        Event outEvent = Event.create()
            .type(EventType.TokenEvent.name())
            .subType(EventSubType.TokenMoved.name())
            .source(token.getId().toString())
            .target(getCurrentZoneRenderer().getZone().getName())
            .oldValue(pathArr.get(0))
            .value(pathArr.get(pathArr.size()-1))
            .additionalInfo(pathCoordinates);
        raiseEvent(outEvent, true);
      }
    }
  }
  
  @Override
  public void modelChanged(ModelChangeEvent event) {
    // Called when Zone changes the model
    //   - when initiativeList is changed
    
    /* In Zone: public enum Event {
        TOKEN_ADDED,
        TOKEN_REMOVED,
        TOKEN_CHANGED,
        GRID_CHANGED,
        DRAWABLE_ADDED,
        DRAWABLE_REMOVED,
        FOG_CHANGED,
        LABEL_ADDED,
        LABEL_REMOVED,
        LABEL_CHANGED,
        TOPOLOGY_CHANGED,
        INITIATIVE_LIST_CHANGED,
        BOARD_CHANGED
    }*/
    String eventType = "ModelEvent";
    String eventSubType = "Change";
    Object eventSource = event.getModel().getClass().getSimpleName();
    Object eventTarget;
    if (event.getArg() != null) {
      eventTarget = event.getArg();
    } else {
      eventTarget = null;
    }
    Object eventOldValue = null;
    Object eventValue = event.getArg();
    String additionalInfo = event.getEvent().toString();
    
    if (Zone.Event.INITIATIVE_LIST_CHANGED.equals(event.getEvent())) {
      eventTarget = InitiativeList.class.getSimpleName();
      eventValue = ((Zone)event.getModel()).getInitiativeList();
      // new zone list, register this to it 
      registerThisToInitiativeList();
      // this event does not have any details, so don't use it to raise anything
    } else {
      // log unhandled event
      // BEFORE x,y,lastPath changed on token, then even originPoint is changed
      // 1st token.changed or added from server command (zone.puttoken), arg token, model zone 
      //   happens in zone renderer commit move just before 2nd
      // 2nd token.changed from zone renderer.commitMove, arg token, model zone
      // NOW maybe deny moves in zone renderer.commitMove
      //   but only for token visible and on token layer (1 unqiue function on[multiple]TokenMove is called)
      //   if deny current to lastPath origin, lastX/lastY to current and lastPath nulled
      // DENY: could be called again in case of deny move
      // 3rd token.changed from fogutil.exposelastpath (zone.puttoken), arg token, model zone
      logEvent(eventType, eventSubType, eventSource, eventTarget, eventOldValue, eventValue, additionalInfo);
    }
  }
  
  protected void logEvent(String eventType, String eventSubType, Object eventSource, Object eventTarget,
    Object eventOldValue, Object eventValue, String additionalInfo) {
    // in case there was no specific event created just create one for logging
    Event outEvent = Event.create()
      .type(eventType).subType(eventSubType)
      .source(eventSource).target(eventTarget).oldValue(eventOldValue).value(eventValue).additionalInfo(additionalInfo);
    
    logEvent(outEvent);
  }
  
  protected void logEvent(Event outEvent) {
    System.out.println(LocalDateTime.now() + " EVENT: " + outEvent);
  }

  // METHOD TO REGISTER FOR EVENTS
  // -----------------------------
  
  private void registerThisToZone() {
    // add this to 
    ZoneRenderer currentZoneRenderer = MapTool.getFrame().getCurrentZoneRenderer();
    if (currentZoneRenderer != null) {
      currentZoneRenderer.getZone().addModelChangeListener(this);
      
      // workaround to get token moved events
      MapTool.getFrame().getToolbox().setTargetRenderer(new DelegateZoneRenderer(currentZoneRenderer, this));
    }
  }
  
  private void registerThisToInitiativeList() {
    InitiativeList initiativeList = MapTool.getFrame().getCurrentZoneRenderer().getZone().getInitiativeList();
    // workaround as there is no check if already added
    // and it can be added more than once
    initiativeList.removePropertyChangeListener(this);
    // once initiativeList is updated a new list is create, so this is only valid ones
    initiativeList.addPropertyChangeListener(this);
  }

  @Override
  public void componentAdded(ContainerEvent e) {
    // needed to get campaign close/open events
    if (initializingCampaignState == 0 || initializingCampaignState == 3) {
      System.out.println("added campaign");
      initializingCampaignState = 1;
      EventSubType subType;
      if (AppState.getCampaignFile() == null) {
        subType = EventSubType.CampaignNew;
      } else {
        subType = EventSubType.CampaignLoaded;
      }
      Event event = Event.create()
      .type(EventType.CampaignEvent.name()).subType(subType.name())
      .source(null).target(null).oldValue(null).value(null).additionalInfo(null);
      raiseEvent(event, false);
    } else if (initializingCampaignState == 2){
      System.out.println("still initializing");
      initializingCampaignState = 3;
    } else {
      System.out.println("UNKNOWN STATE");
    }
  }

  @Override
  public void componentRemoved(ContainerEvent e) {
    // needed to get campaign close/open events
    if (AppState.getCampaignFile() == null) {
      // new campaign
      if (initializingCampaignState == -1) {
        System.out.println("maptool starting");
        Event event = Event.create()
        .type(EventType.ApplicationEvent.name()).subType(EventSubType.ApplicationStarting.name())
        .source(null).target(null).oldValue(null).value(null).additionalInfo(null);
        raiseEvent(event, false);
        initializingCampaignState = 0;
      } else if (initializingCampaignState == 1) {
        System.out.println("closing campaign 1");
        initializingCampaignState = 0;
      }
    } else {
      // loading campaign
      if (initializingCampaignState == -1) {
        System.out.println("maptool starting");
        initializingCampaignState = 0;
      } else if (initializingCampaignState == 1) {
        System.out.println("closing campaign 2");
        initializingCampaignState = 2;
      } else {
        System.out.println("initializing campaign");
        initializingCampaignState = 0;
      }
    }
  }
    
  // HELPER METHODS AND CLASSES
  // --------------------------
  
  private JSONArray pathPointsToJSONArray(final List<Map<String, Integer>> pathPoints) {
    JSONArray jsonArr = new JSONArray();
    if (pathPoints == null || pathPoints.isEmpty()) {
      return jsonArr;
    }
    JSONObject pointObj = new JSONObject();
    // Lee: had to add handling for the line segment made by unsnapped movedOverToken()
    if (pathPoints.get(0).containsKey("x"))
      for (Map<String, Integer> entry : pathPoints) {
        pointObj.element("x", entry.get("x"));
        pointObj.element("y", entry.get("y"));
        jsonArr.add(pointObj);
      }
    else
      for (Map<String, Integer> entry : pathPoints) {
        pointObj.element("x1", entry.get("x1"));
        pointObj.element("y1", entry.get("y1"));
        pointObj.element("x2", entry.get("x2"));
        pointObj.element("y2", entry.get("y2"));
        jsonArr.add(pointObj);
      }

    return jsonArr;
  }

  private List<Map<String, Integer>> getLastPathList(
      final Path<?> path, final boolean useDistancePerCell) {
    List<Map<String, Integer>> points = new ArrayList<Map<String, Integer>>();
    if (path != null) {
      Zone zone = MapTool.getFrame().getCurrentZoneRenderer().getZone();
      AbstractPoint zp = null;

      for (Object pathCells : path.getCellPath()) {

        if (pathCells instanceof CellPoint) {
          CellPoint cp = (CellPoint) pathCells;
          if (useDistancePerCell) {
            zp = zone.getGrid().convert((CellPoint) pathCells);
          } else {
            zp = cp;
          }
        } else {
          zp = (ZonePoint) pathCells;
        }
        if (zp != null) {
          Map<String, Integer> tokenLocationPoint = new HashMap<String, Integer>();
          tokenLocationPoint.put("x", Integer.valueOf(zp.x));
          tokenLocationPoint.put("y", Integer.valueOf(zp.y));
          points.add(tokenLocationPoint);
        }
      }
    }
    return points;
  }
  
  public static interface EventHandler {
    void handleEvent(Event event);
    boolean handleVetoableEvent(Event event);
  }
  
  public static class Event {
    private String eventType;
    private String eventSubType;
    private Object eventSource;
    private Object eventTarget;
    private Object eventOldValue;
    private Object eventValue;
    private String additionalInfo;
    
    public static Event create() {
      return new Event();
    }
    
    public static Event create(String eventType, String eventSubType, Object eventSource, Object eventTarget, Object eventOldValue,
        Object eventValue, String additionalInfo) {
      return new Event(eventType, eventSubType, eventSource, eventTarget, eventOldValue, eventValue, additionalInfo);
    }
    
    Event() {
    }

    public Event(String eventType, String eventSubType, Object eventSource, Object eventTarget, Object eventOldValue,
        Object eventValue, String additionalInfo) {
      super();
      this.eventType = eventType;
      this.eventSubType = eventSubType;
      this.eventSource = eventSource;
      this.eventTarget = eventTarget;
      this.eventOldValue = eventOldValue;
      this.eventValue = eventValue;
      this.additionalInfo = additionalInfo;
    }

    public String getEventType() {
      return eventType;
    }

    public String getEventSubType() {
      return eventSubType;
    }

    public Object getEventSource() {
      return eventSource;
    }

    public Object getEventTarget() {
      return eventTarget;
    }

    public Object getEventOldValue() {
      return eventOldValue;
    }

    public Object getEventValue() {
      return eventValue;
    }

    public String getAdditionalInfo() {
      return additionalInfo;
    }

    Event type(String eventType) {
      this.eventType = eventType;
      return this;
    }

    Event subType(String eventSubType) {
      this.eventSubType = eventSubType;
      return this;
    }

    Event source(Object eventSource) {
      this.eventSource = eventSource;
      return this;
    }

    Event target(Object eventTarget) {
      this.eventTarget = eventTarget;
      return this;
    }

    Event oldValue(Object eventOldValue) {
      this.eventOldValue = eventOldValue;
      return this;
    }

    Event value(Object eventValue) {
      this.eventValue = eventValue;
      return this;
    }

    Event additionalInfo(String additionalInfo) {
      this.additionalInfo = additionalInfo;
      return this;
    }

    @Override
    public String toString() {
      return "Event [eventType=" + eventType + ", eventSubType=" + eventSubType + ", eventSource=" + eventSource
          + ", eventTarget=" + eventTarget + ", eventOldValue=" + eventOldValue + ", eventValue=" + eventValue
          + ", additionalInfo=" + additionalInfo + "]";
    }    
  }
}
