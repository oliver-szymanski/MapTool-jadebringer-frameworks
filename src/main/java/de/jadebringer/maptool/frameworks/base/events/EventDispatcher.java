package de.jadebringer.maptool.frameworks.base.events;

import net.rptools.lib.AppEvent;
import net.rptools.lib.AppEventListener;
import net.rptools.maptool.client.MapTool;

/** 
 * Capture the spread events in any possible way and 
 * distribute it further.
 * 
 * @author naciron
 *
 */
public class EventDispatcher implements AppEventListener {

  private static final EventDispatcher instance = new EventDispatcher();
  
  protected EventDispatcher() {
    init();
  }
  
  public static EventDispatcher getInstance() {
    return instance;
  }
  
  protected void init() {
    MapTool.getEventDispatcher().addListener(this);
  }
  
  protected void onZoneChanged() {
    
  }
  
  protected void onInitiativeChanged() {
    
  }
  
  @Override
  public void handleAppEvent(AppEvent event) {
    System.out.println("got event" + event);
  }
  
}
