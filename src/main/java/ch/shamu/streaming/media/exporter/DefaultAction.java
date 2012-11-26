package ch.shamu.streaming.media.exporter;

import net.sourceforge.stripes.action.ActionBean;

public class DefaultAction implements ActionBean {

	private ch.shamu.streaming.media.exporter.ActionBeanContext context;

	
	public net.sourceforge.stripes.action.ActionBeanContext getContext() {
		return context;
	}

	public void setContext(net.sourceforge.stripes.action.ActionBeanContext context) {
		this.context = (ch.shamu.streaming.media.exporter.ActionBeanContext) context;
	}

}
