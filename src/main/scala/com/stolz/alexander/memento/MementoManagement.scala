package com.stolz.alexander.memento

import scala.collection.mutable.HashMap
import scala.collection.mutable.Stack 
import com.stolz.alexander.memento.exceptions.MementoException

object MementoManagement {
  /**
   * Currently saved objects
   */
  private var objectHash = new HashMap[Memento,StateObject]()
  
  /**
   * Saved snapshots
   */
  private var undoStack = new Stack[HashMap[Memento,StateObject]]()
  
  /**
   * The current time (not real time but state time 0..5..N)
   */
  private var currentTimeStamp = 0L
  
  /**
   * Getter for the current time (not real time but state time 0..5..N)
   */
  def getCurrentTimeStamp:Long = currentTimeStamp

  /**
   * Add object to be persisted with the next call to createNewMementoState
   */
  def addStateObject(m:Memento) {objectHash.put(m, m.getState)}
  
  /**
   * A saved state is present which can be used to go back in "time"
   */
  def hasMementoableAction:Boolean = !undoStack.isEmpty
  
  /**
   * Delete all saved states
   */
  def resetMementoManagement() {
    createNewMementoState()
    undoStack.clear()
    objectHash.clear()
    currentTimeStamp = 0
  }
  
  /**
   * Create snapshot
   */
  def createNewMementoState() {
    undoStack.push(objectHash)
    objectHash.clear()
    currentTimeStamp += 1
  }
  
  /**
   * Revert to time t
   */
  def undoUntil(t:Long) { while(currentTimeStamp > t)undo() }
  
  /**
   * Revert to the previous saved state
   */
  def undo() {
    if (undoStack.isEmpty) new MementoException("Nothing to undo")
    
    var lastState = undoStack.pop()
    for ((m:Memento,mo:StateObject)<- lastState) m.setState(mo)
    
    /*Set new current timestamp*/
    currentTimeStamp -= 1
  }
}

