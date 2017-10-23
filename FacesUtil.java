
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.faces.FactoryFinder;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.FacesContextFactory;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.lifecycle.LifecycleFactory;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.primefaces.context.RequestContext;
import org.primefaces.model.UploadedFile;

public class FacesUtil {
	public static void adicionarMsgSave() {
		
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Operação Concluída", "Registro Salvo com sucesso!"));
	}

	public static boolean isPostback() {
		return FacesContext.getCurrentInstance().isPostback();
	}

	public static String uploadDeArquivo(String pasta, UploadedFile uploadedFile) throws IOException {
		System.out.println( filePath(pasta, uploadedFile.getFileName()));
		File file = new File( filePath(pasta, uploadedFile.getFileName()) );
		if(!file.exists()){file.mkdirs();}
		
		FileOutputStream out = new FileOutputStream(file);
		out.write(uploadedFile.getContents());
		out.close();
		return file.getPath();
	}
	
	public static String filePath(String pasta, String filename) {
		return getRealPah("") + Util.separator + "WEB-INF"	+ Util.separator + pasta + Util.separator + filename;
	}
	public static void adicionarMsgUpdate() {
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Operação Concluída", "Registro Atualizado com sucesso!"));
	}

	public static void adicionarMsgDelete() {
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Operação Concluída", "Registro Excluido com sucesso!"));
	}

	public static void adicionarMsgError(String msgError) {
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_ERROR, msgError, ""));
	}

	public static void adicionarMsgInfo(String msgInfo) {
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, msgInfo, "Informação"));
	}

	public static HttpServletResponse getResponse() {
		return (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
	}

	public static HttpServletRequest getRequest() {
		return (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
	}
	
	public static void msgInDialog(Severity severity, String summary, String detail) {
		RequestContext.getCurrentInstance().showMessageInDialog(new FacesMessage(severity, summary, detail));
	}
	
	
	public static void msgInDialogSucesso(String titulo, String msg) {
		RequestContext.getCurrentInstance().showMessageInDialog(new FacesMessage(FacesMessage.SEVERITY_INFO, titulo, msg));
	}

	
	public static void msgInDialogError(String titulo, String msg) {
		RequestContext.getCurrentInstance().showMessageInDialog(new FacesMessage(FacesMessage.SEVERITY_FATAL, titulo, msg));
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
	
	
	public static FacesContext getContext() {
		return FacesContext.getCurrentInstance();
	}
	
	public static String getRealPah(String path) {
		return FacesContext.getCurrentInstance().getExternalContext().getRealPath(path);
	}

	public static ExternalContext getExternalContext() {
		return FacesContext.getCurrentInstance().getExternalContext();
	}
	
	public static String getContextPath() {
		return FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
	}
	
	public static void redirect(String url) throws IOException{
		FacesContext.getCurrentInstance().getExternalContext().redirect(FacesUtil.getContextPath()+url);
	}
	public static void redirectSemContext(String url) throws IOException{
		FacesContext.getCurrentInstance().getExternalContext().redirect(url);
	}

	public static String getPaginaAtual() {
		return FacesContext.getCurrentInstance().getViewRoot().getViewId();
	}


	
	public static FacesContext getFacesContext(ServletRequest request,ServletResponse response) {
	    FacesContext facesContext = FacesContext.getCurrentInstance();
	    if (facesContext == null) {
	        LifecycleFactory lifecycleFactory = (LifecycleFactory) FactoryFinder.getFactory(FactoryFinder.LIFECYCLE_FACTORY); 
	        Lifecycle lifecycle = lifecycleFactory.getLifecycle(LifecycleFactory.DEFAULT_LIFECYCLE);
	        FacesContextFactory facesContextFactory = (FacesContextFactory) FactoryFinder.getFactory(FactoryFinder.FACES_CONTEXT_FACTORY);
	        facesContext = facesContextFactory.getFacesContext(request.getServletContext(), request, response, lifecycle);
	        facesContext.setViewRoot(facesContext.getApplication().getViewHandler().createView(facesContext, ""));
	        FacesContextWrapper.setCurrentInstance(facesContext);
	    }
	    return facesContext;
	}

	
	private static abstract class FacesContextWrapper extends FacesContext {
	    protected static void setCurrentInstance(FacesContext facesContext) {
	        FacesContext.setCurrentInstance(facesContext);
	    }
	    
	    
	    
	}
	
	
	
}
