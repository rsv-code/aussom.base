/*
 * Copyright 2017 Austin Lehman
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.aussom.ast;

import com.aussom.Environment;
import com.aussom.types.AussomException;
import com.aussom.types.AussomException.exType;
import com.aussom.types.AussomInt;
import com.aussom.types.AussomList;
import com.aussom.types.AussomMap;
import com.aussom.types.AussomNull;
import com.aussom.types.AussomObject;
import com.aussom.types.AussomRef;
import com.aussom.types.AussomType;
import com.aussom.types.AussomTypeInt;
import com.aussom.types.cType;

public class astObj  extends astNode implements astNodeInt {
	private astNode index = null;
	
	//private Map<String, objContainer> members = new ConcurrentHashMap<String, objContainer>();
	
	public astObj() {
		this.setType(astNodeType.OBJ);
	}
	
	public void setIndex(astNode Index) {
		this.index = Index;
	}
	
	public astNode getIndex() {
		return this.index;
	}
	
	@Override
	public AussomType evalImpl(Environment env, boolean getRef) throws aussomException {
		AussomType ret = new AussomNull();
		
		if (env.getCurObj() == null) {
			ret = this.evalObjStart(env, getRef);
		} else {
			ret = this.evalObj(env, getRef);
		}

		return ret;
	}
	
	/**
	 * This method is called if the envorinment current object isn't set. This 
	 * is the start of the object path.
	 * @param env The current Environment object.
	 * @param getRef a boolean with true to get reference or false for not.
	 * @return A AussomType object.
	 * @throws aussomException
	 */
	private AussomType evalObjStart(Environment env, boolean getRef) throws aussomException {
		AussomType ret = new AussomNull();
		
		// this object
		if (this.getName().equals("this")) {
		  // We need to get the object pointer from the 
		  // current object.
		  if (this.getChild() != null) {
			// Set the current object to this class instance.
			Environment tenv = env.clone(env.getClassInstance());
			ret = this.getChild().eval(tenv, getRef);
		  } else {
			return env.getClassInstance();
		  }
		}
		
		// Found in locals.
		else if (!getRef && env.getLocals().contains(this.getName())) {
		  if (this.getChild() != null) {
			Environment tenv = env.clone(env.getLocals().get(this.getName()));
			ret = this.getChild().eval(tenv, getRef);
		  } else {
			return env.getLocals().get(this.getName());
		  }
		}
		
		// Static object found.
		else if (env.getEngine().containsStaticClass(this.getName())) {
			if (this.getChild() != null) {
				// We need to get the extern static class.
				if (this.getChild().getType() == astNodeType.FUNCTCALL || this.getChild().getType() == astNodeType.OBJ) {
					Environment tenv = env.clone(env.getEngine().getStaticClass(this.getName()));
					ret = this.getChild().eval(tenv, getRef);
				} else {
					AussomException e = new AussomException(exType.exInternal);
					e.setException(getLineNum(), "NOT_IMPLEMENTED", "aObj.callObjStart(): Static class with child of '" + this.getChild().getType().name() + "' found.", env.getCallStack().getStackTrace());
					return e;
				}
			} else {
				AussomException e = new AussomException(exType.exRuntime);
				e.setException(getLineNum(), "NO_OPERATION", "aObj.callObjStart(): Static class object found but no child function or property provided.", env.getCallStack().getStackTrace());
				return e;
			}
		}
		
		// If getRef is true and we have no children, then just add to locals.
		else if (getRef && this.getChild() == null) {
		  if (!env.getLocals().contains(this.getName())) {
			env.getLocals().add(this.getName(), new AussomNull());
		  }
		  AussomRef ref = new AussomRef();
		  ref.setMap(this.getName(), env.getLocals().getMap());
		  ret = ref;
		}
		
		else if (getRef && this.getChild() != null && env.getLocals().contains(this.getName())) {
		  Environment tenv = env.clone(env.getLocals().get(this.getName()));
		  ret = this.getChild().eval(tenv, getRef);
		}
		
		// Else, don't know what to do here.
		else {
		  AussomException e = new AussomException(exType.exInternal);
		  e.setException(getLineNum(), "NOT_IMPLEMENTED", "aObj.callObjStart(): Unmatched object type for '" + this.getName() + "'.", env.getCallStack().getStackTrace());
		  return e;
		}
		
		return ret;
	}
	
	private AussomType evalObj (Environment env, boolean getRef) throws aussomException {
		AussomType ret = new AussomNull();
		
		// Found that it exists in the current object.
		if (env.getCurObj() instanceof AussomObject && ((AussomObject)env.getCurObj()).getMembers().contains(this.getName())) {
		  // Check member access.
		  if (this.memberHasAccess(env, this.getName())) {
			if (this.getChild() != null) {
			  Environment tenv = env.clone(((AussomObject)env.getCurObj()).getMembers().get(this.getName()));
			  ret = this.getChild().eval(tenv, getRef);
			} else {
			  if (getRef) {
				AussomRef ref = new AussomRef();
				ref.setMap(this.getName(), ((AussomObject)env.getCurObj()).getMembers().getMap());
				ret = ref;
			  } else {
				ret = ((AussomObject)env.getCurObj()).getMembers().get(this.getName());
			  }
			}
		  } else {
			AussomException e = new AussomException(exType.exRuntime);
			e.setException(getLineNum(), "NO_ACCESS", "aObj.callObj(): No access to member '" + this.getName() + "'.", env.getCallStack().getStackTrace());
			return e;
		  }
		}
		
		// Found in current map object.
		else if (env.getCurObj().getType() == cType.cMap) {
			String key = this.getName();
			if (this.getIndex() != null) {
				Environment tenv = env.clone(((AussomMap)env.getCurObj()).getValue().get(key));
				key = ((AussomTypeInt)this.getIndex().eval(tenv, false)).str();
			}
			
			if (getRef) {
				// Not found, but we're going to return a ref anyway.
				AussomRef ref = new AussomRef();
				ref.setMap(key, ((AussomMap)env.getCurObj()).getValue());
				ret = ref;
			} else if (((AussomMap)env.getCurObj()).contains(key)) {
				// Found that it exists in this map.
				if (this.getChild() != null) {
					Environment tenv = env.clone(((AussomMap)env.getCurObj()).getValue().get(key));
					ret = this.getChild().eval(tenv, getRef);
				} else {
					ret = ((AussomMap)env.getCurObj()).getValue().get(key);
				}
			} else {
				AussomException e = new AussomException(exType.exRuntime);
				e.setException(this.getLineNum(), "MAP_MISSING_KEY", "aObj.callObj(): Map doesn't have key '" + key + "'.", env.getCallStack().getStackTrace());
				return e;
			}
		}
		
		// Found in current list object.
		else if (env.getCurObj().getType() == cType.cList) {
		  // Found that it exists in this map.
		  if (this.index != null) {
			Environment ienv = env.clone(null);
			AussomType ctindex = this.index.eval(ienv, false);
			if (ctindex.isEx()) {
				return ctindex;
			}
			if (ctindex.getType() == cType.cInt) {
			  long ind = ((AussomInt)ctindex).getValue();
			  AussomList lst = (AussomList)env.getCurObj();
			  if (ind >= 0 && ((long)ind) < lst.getValue().size()) {
				if (this.getChild() == null) {
				  ret = lst.getValue().get((int)ind);
				} else {
				  Environment tenv = env.clone(lst.getValue().get((int)ind));
				  ret = this.getChild().eval(tenv, getRef);
				}
			  } else {
				AussomException e = new AussomException(exType.exRuntime);
				e.setException(this.getLineNum(), "INDEX_OUT_OF_BOUNDS", "aObj.callObj(): Index out of bounds.", env.getCallStack().getStackTrace());
				return e;
			  }
			} else {
			  AussomException e = new AussomException(exType.exRuntime);
			  e.setException(this.getLineNum(), "INDEX_NOT_FOUND", "aObj.callObj(): Provided index isn't an iteger value.", env.getCallStack().getStackTrace());
			  return e;
			}
		  } else {
			AussomException e = new AussomException(exType.exRuntime);
			e.setException(this.getLineNum(), "INDEX_NOT_FOUND", "aObj.callObj(): List found but no index found.", env.getCallStack().getStackTrace());
			return e;
		  }
		} else {
		  AussomException e = new AussomException(exType.exInternal);
		  e.setException(this.getLineNum(), "NOT_IMPLEMENTED", "aObj.callObj(): Unmatched object type for '" + this.getName() + "'.", env.getCallStack().getStackTrace());
		  return e;
		}
		
		return ret;
	}
	
	private boolean memberHasAccess (Environment env, String memberName) {
		if (env.getClassInstance() != env.getCurObj()) {
			astClass ac = ((AussomObject)env.getCurObj()).getClassDef();
			if (ac.containsMember(memberName)) {
			  if (ac.getMember(memberName).getAccessType() == AccessType.aPrivate) {
				return false;
			  } else {
				return true;
			  }
			} else {
			  return true;
			}
		  } else {
			return true;
		  }
	}
	
	@Override
	public String toString(int Level) {
		String rstr = "";
		rstr += getTabs(Level) + "{\n";
		rstr += this.getNodeStr(Level + 1) + ",\n";
		if(this.index != null) {
		  rstr += getTabs(Level + 1) + "\"index\":\n";
			rstr += ((astNodeInt)this.index).toString(Level + 1) + ",\n";
		}
		if(this.getChild() != null) {
			rstr += getTabs(Level + 1) + "\"child\":\n";
			rstr += ((astNodeInt)this.getChild()).toString(Level + 1) + ",\n";
		}
		rstr += getTabs(Level) + "}";
		return rstr;
	}
}
