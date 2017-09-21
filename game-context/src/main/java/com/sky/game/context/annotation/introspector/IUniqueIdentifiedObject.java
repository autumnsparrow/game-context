/**
 * @sparrow
 * @Mar 3, 2015   @4:00:43 PM
 * @coptyright Beijing BZWT Technology Co ., Ltd .
 */
package com.sky.game.context.annotation.introspector;

import com.sky.game.context.annotation.introspector.IIdentifiedObject;

/**
 * @author sparrow
 *
 */
public interface IUniqueIdentifiedObject extends IIdentifiedObject {
	
	
	long getParentId();
	boolean equals(IUniqueIdentifiedObject obj);

}
