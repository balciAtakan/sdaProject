package sda.web.backingbeans;

import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.component.html.HtmlInputText;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("request")
public class NotizenBean {

	// Daten - ueblicherweise in NotizenView
	private String notiz;
	private String notizanzeige;

	public String getNotiz() {
		System.out.println("getn");
		return notiz;
	}

	public void setNotiz(String notiz) {
		System.out.println("setn");
		this.notiz = notiz;
		this.notizanzeige = notiz;
	}

	public String getNotizanzeige() {
		System.out.println("getna");
		return notizanzeige;
	}

	public void setNotizanzeige(String notizanzeige) {
		System.out.println("setna");
		this.notizanzeige = notizanzeige;
	}

	// (Action)Listener-Methoden
	public void zeigeStatusDerModelleAmServer(ActionEvent ae) {
		System.out.println(">> zeigeStatusDerModelleAmServer");
		
		// Ich klicke auf den Button und moechte Zugriff auf das Textfeld form1:notizInput...
		UIViewRoot uiViewRoot = FacesContext.getCurrentInstance().getViewRoot();
		UIComponent uiComponent =
				uiViewRoot.findComponent("form1:notizInput");
	
		HtmlInputText htmlInputText = (HtmlInputText) uiComponent;
		System.out.println("Submitted Value Notiz: " + htmlInputText.getSubmittedValue());
		System.out.println("Local Value Notiz: " + htmlInputText.getLocalValue());
		System.out.println("Modellwert Notiz: " + this.notiz);
	}
}
