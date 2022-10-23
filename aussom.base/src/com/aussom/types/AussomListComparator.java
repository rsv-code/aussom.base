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

package com.aussom.types;

import java.util.Comparator;

import com.aussom.ast.aussomException;

@SuppressWarnings("rawtypes")
public class AussomListComparator implements Comparator {
	public enum SortOrder { ASCENDING, DESCENDING, CUSTOM }
	
	private SortOrder order = SortOrder.DESCENDING;
	public void setSortOrder(SortOrder Order) { this.order = Order; }
	
	private AussomCallback onCompare = null;
	public void setCallback(AussomCallback OnCompare) { this.onCompare = OnCompare; }
	
	@Override
	public int compare(Object o1, Object o2) {
		Integer res = 0;
		AussomType one = (AussomType)o1;
		AussomType two = (AussomType)o2;
		
		if(this.order == SortOrder.CUSTOM) {
			try {
				return this.customCompare(one, two);
			} catch (aussomException e) {
				throw new RuntimeException(e.getMessage());
			}
		} else {
			// Comparing same types
			if(one.getType() == two.getType()) {
				if(one instanceof AussomString) {
					String vone = ((AussomString)one).getValue();
					String vtwo = ((AussomString)two).getValue();
					res = vone.compareTo(vtwo);
				} else if(one instanceof AussomBool) {
					Boolean vone = ((AussomBool)one).getValue();
					Boolean vtwo = ((AussomBool)two).getValue();
					res = vone.compareTo(vtwo);
				} else if(one instanceof AussomInt) {
					Long vone = ((AussomInt)one).getValue();
					Long vtwo = ((AussomInt)two).getValue();
					res = vone.compareTo(vtwo);
				} else if(one instanceof AussomDouble) {
					Double vone = ((AussomDouble)one).getValue();
					Double vtwo = ((AussomDouble)two).getValue();
					res = vone.compareTo(vtwo);
				} else if(
						one instanceof AussomList 
						|| one instanceof AussomMap
						|| one.getType() == cType.cCallback
					) {
					String vone = ((AussomTypeInt)one).str();
					String vtwo = ((AussomTypeInt)two).str();
					res = vone.compareTo(vtwo);
				} else if(one.getType() == cType.cObject) {
					String vone = ((AussomObject)one).getClassDef().getName();
					String vtwo = ((AussomObject)two).getClassDef().getName();
					res = vone.compareTo(vtwo);
				} else if(one.isNull()) {
					res = 0;
				}
			} else if(one instanceof AussomInt && two instanceof AussomDouble) {
				Double vone = (double)((AussomInt)one).getValue();
				Double vtwo = ((AussomDouble)two).getValue();
				res = vone.compareTo(vtwo);
			} else if(one instanceof AussomDouble && two instanceof AussomInt) {
				Double vone = ((AussomDouble)one).getValue();
				Double vtwo = (double)((AussomInt)two).getValue();
				res = vone.compareTo(vtwo);
			} else if(one.isNull()) {
				res = -1;
			} else if(two.isNull()) {
				res = 1;
			} else {
				String vone = ((AussomTypeInt)one).str();
				String vtwo = ((AussomTypeInt)two).str();
				res = vone.compareTo(vtwo);
			}
			
			if(this.order == SortOrder.DESCENDING) return res;
			else return res * -1;
		}
	}
	
	private int customCompare(AussomType one, AussomType two) throws aussomException
	{
		int ret = 0;
		if(this.onCompare != null) {
			AussomList args = new AussomList();
			args.add(one);
			args.add(two);
			AussomType retInt = this.onCompare.call(this.onCompare.getEnv(), args);
			if(retInt instanceof AussomInt)
				ret = (int) ((AussomInt)retInt).getValue();
			else
				throw new aussomException("AussomListComparator.compareCustom(): Result from callback expects type of int, but found " + retInt.getType().name() + " instead.");
		}
		else
			throw new aussomException("AussomListComparator.compareCustom(): Callback OnCompare is null.");
		
		return ret;
	}
}
