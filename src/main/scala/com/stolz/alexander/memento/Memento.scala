package com.stolz.alexander.memento

/**
* This trait is implemented by classes which have a state that needs to be persistable
* and revertable
**/
trait Memento {
    def getState():StateObject
    def setState(so: StateObject)
}
