package com.sky.game.context.configuration.ice;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.sky.game.context.util.GameUtil;
@JsonRootName(value="Config-List")
public class IceServiceConfigIndex {
	
	@JacksonXmlElementWrapper(localName="Config")
	List<IceServiceConfigInfo>  index;

	public IceServiceConfigIndex() {
		// TODO Auto-generated constructor stub
		index=GameUtil.getList();
	}

	public List<IceServiceConfigInfo> getIndex() {
		return index;
	}

	public void setIndex(List<IceServiceConfigInfo> index) {
		this.index = index;
	}
	
	

}
