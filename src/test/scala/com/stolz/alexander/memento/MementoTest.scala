package com.stolz.alexander.memento


/**
 * Test implementation of a stateful object
 */
case class TestStateContainer(var state: Long) extends Memento {
  def getState():StateObject = (new StateObject) + (0, state)
  def setState(so: StateObject) {state = so :#(0).asInstanceOf[Long]}
}

case class TestSerializableClass(var v:Int, var v2:String)

/**
 * Test implementation of a stateful object
 */
case class TestStateContainerS(var state: TestSerializableClass) extends Memento {
  def getState():StateObject = (new StateObject) ++ (0, state)
  def setState(so: StateObject) {state = so :##(0).asInstanceOf[Long]}
}


object MementoTest {

  def main(args: Array[String]): Unit = {
    
    val t1 = new TestStateContainer(2013)
    val t2 = new TestStateContainer(2014)
    //
    // Single undo test
    //
    //Init test
    assert(t1.state == 2013)
    assert(t2.state == 2014)
    //Create new state
    assert(MementoManagement.getCurrentTimeStamp == 0L)
    MementoManagement.addStateObject(t1)
    MementoManagement.addStateObject(t2)
    MementoManagement.createNewMementoState
    assert(MementoManagement.getCurrentTimeStamp == 1L)
    //Change objects
    t1.state = 3013
    t2.state = 3014
    //Revert to last known state
    MementoManagement.undo
    assert(MementoManagement.getCurrentTimeStamp == 0L) 
    assert(t1.state == 2013)
    assert(t2.state == 2014)
    
    //
    // Multi undo test
    //
    //Create new state
    assert(MementoManagement.getCurrentTimeStamp == 0L)
    MementoManagement.addStateObject(t1)
    MementoManagement.addStateObject(t2)
    MementoManagement.createNewMementoState
    assert(MementoManagement.getCurrentTimeStamp == 1L)
    //Change objects
    t1.state = 3013
    t2.state = 3014
    //Create new state
    assert(MementoManagement.getCurrentTimeStamp == 1L)
    MementoManagement.addStateObject(t1)
    MementoManagement.addStateObject(t2)
    MementoManagement.createNewMementoState
    assert(MementoManagement.getCurrentTimeStamp == 2L)
    //Change objects
    t1.state = 3013
    t2.state = 3014
    //Create new state
    assert(MementoManagement.getCurrentTimeStamp == 2L)
    MementoManagement.addStateObject(t1)
    MementoManagement.addStateObject(t2)
    MementoManagement.createNewMementoState
    assert(MementoManagement.getCurrentTimeStamp == 3L)
    //Revert to state with time 1
    MementoManagement.undoUntil(0)
    assert(MementoManagement.getCurrentTimeStamp == 0L) 
    assert(t1.state == 2013)
    assert(t2.state == 2014)
    
    //
    // Object serialization test
    //
    
    //Create and persist state
    val t3 = TestStateContainerS(TestSerializableClass(0, "TEST123"))
    assert(MementoManagement.getCurrentTimeStamp == 0L) 
    MementoManagement.addStateObject(t3)
    MementoManagement.createNewMementoState
    assert(MementoManagement.getCurrentTimeStamp == 1L)
    t3.state.v = 2
    t3.state.v2 = "TEST321"
    //Revert to last known state
    MementoManagement.undo
    assert(MementoManagement.getCurrentTimeStamp == 0L) 
    assert(t3.state.v == 0)
    assert(t3.state.v2.equals("TEST123"))
  }

}