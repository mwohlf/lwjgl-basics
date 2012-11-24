package net.wohlfart.widgets;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

public class WidgetSet {

	Collection<AbstractWidget> toplevelWidgets = new HashSet<AbstractWidget>();
	
	
	public WidgetSet() {
		toplevelWidgets.add(new PanelWidget(10,10,110,110));
	}

	public void paint(Renderer renderer) {
		Iterator<AbstractWidget> iter = toplevelWidgets.iterator();
		while (iter.hasNext()) {
			iter.next().paint(renderer);
		}
	}

}
