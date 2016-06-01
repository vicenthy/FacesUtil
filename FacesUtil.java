package seupackage;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.primefaces.context.RequestContext;

public class FacesUtil {
	public static void adicionarMsgSave() {
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Operação Concluída", "Registro Salvo com sucesso!"));
	}
	public static boolean isPostback(){
		return FacesContext.getCurrentInstance().isPostback();
	}
	public static void adicionarMsgUpdate() {
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Operação Concluída", "Registro Atualizado com sucesso!"));
	}
	public static void adicionarMsgDelete() {
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Operação Concluída", "Registro Excluído com sucesso!"));
	}
	public static void adicionarMsgError(String msgError) {
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, msgError, "Error!"));
	}
	public static void adicionarMsgInfo(String msgInfo) {
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, msgInfo, "Informação"));
	}
	public static HttpServletResponse getResponse(){
		return (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
	}
	public static HttpServletRequest getRequest(){
		return (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
	}
	public static void MsgInDialog(String summary, String detail ) {
		RequestContext.getCurrentInstance().showMessageInDialog(new FacesMessage(summary, detail));
	}
	public static Object getFlash(String objeto) throws ClassCastException {
		return FacesContext.getCurrentInstance().getExternalContext().getFlash().get(objeto);
	}
	public static Object putFlash(String alias, Object objeto) throws ClassCastException {
		return FacesContext.getCurrentInstance().getExternalContext().getFlash().put(alias, objeto);
	}
	public static String getParam(String param) {
		return FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get(param);
	}
	public static String putParam(String name, String param) {
		return FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().put(name, param);
	}
	public static String getBrowser() {
		return FacesContext.getCurrentInstance().getExternalContext().getRequestHeaderMap().get("User-Agent");
	}

	
	
	
	
}
