package com.stolz.alexander.memento

import scala.collection.mutable.HashMap
import java.io.ByteArrayInputStream
import java.io.ObjectInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectOutputStream

/**
* Captures the state of an Object
**/
class StateObject {
  /**
  * Value map of the StateObject
  **/
  private val dataMap = new HashMap[Long, Any]
  
  /**
  * The StateObject has the following key
  **/
  def hasKey(id:Long):Boolean = dataMap.contains(id)

  /**
  * Add value to state object
  **/
  def +[A](id:Long, obj:A):StateObject = {
	    dataMap.put(id, obj)
	this
  }
  
  /**
   * Object serialization version of the + method
   */
  def ++[A](id:Long, obj:A):StateObject = { //throws Exception
		val out = new ByteArrayOutputStream();
		val s = new ObjectOutputStream(out);
		s.writeObject(obj);
		s.flush();	
       	dataMap.put(id, out.toByteArray());
	this
  }

  /**
  * Return the object belonging to the id
  **/
  def :#[A](id:Long):A = dataMap.get(id).get.asInstanceOf[A]
  
  /**
  * Object serialization version of the :# method
  **/
  def :##[A](id:Long):A = {//throws Exception
    val obj = dataMap.get(id).get
    val bytes = obj.asInstanceOf[Array[Byte]];
	val in = new ByteArrayInputStream(bytes);
	val s = new ObjectInputStream(in);
	s.readObject().asInstanceOf[A];
  } 
}
