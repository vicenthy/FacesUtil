package br.com.hypnos.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

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

import org.apache.commons.io.FileUtils;
import org.hibernate.engine.spi.SessionImplementor;
import org.primefaces.PrimeFaces;
import org.primefaces.context.RequestContext;
import org.primefaces.model.UploadedFile;

import br.com.hypnos.config.HibernateUtil;
import br.com.hypnos.util.cdi.MyUtil;

public class FacesUtil {

	public static boolean isNotPostback() {
		return !isPostback();
	}


	@SuppressWarnings("resource")
	public static void convertTXTUnixToWindows(String entrada, String saida) {
		try {
			BufferedWriter writer = new BufferedWriter(
					new OutputStreamWriter(new FileOutputStream(new File(saida)), "ISO-8859-1"));

			new BufferedReader(new FileReader(new File(entrada))).lines().forEach(linha -> {
				try {
					writer.write(linha.replaceAll("\r", "") + "\r\n");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			});

			writer.flush();
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void download(String saida, String format, String contentType, String name) {
		try {
			ExternalContext externalContext = FacesUtil.getExternalContext();
			externalContext.setResponseContentType(contentType);
			externalContext.setResponseHeader("Content-Disposition", "attachment;filename=\"" + name + "\"");
			externalContext.setResponseHeader("Content-Length", String.valueOf(new File(saida).length()));
			HttpServletResponse response = (HttpServletResponse) FacesUtil.getFacesContext().getExternalContext()
					.getResponse();
			OutputStream out = response.getOutputStream();
			out.write(Files.readAllBytes(Paths.get(saida)));
			out.flush();
			out.close();
			FileUtils.deleteQuietly(new File(saida));
			FacesUtil.getContext().responseComplete();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void updateComponent(String id) {
		PrimeFaces.current().ajax().update(id);
	}

	public static void updateComponentRenderId(String id) {
		FacesContext.getCurrentInstance().getPartialViewContext().getRenderIds().add(id);
	}

	public static void addErrorMessage(String message) {
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_ERROR, message, message));
	}

	public static void addInfoMessage(String message) {
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, message, message));
	}

	public static void addWarnMessage(String message) {
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_WARN, message, message));
	}

	public static void addFatalMessage(String message) {
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_FATAL, message, message));
	}

	public static void addMessageInDialog(String message, Severity severity) {
		RequestContext.getCurrentInstance().showMessageInDialog(new FacesMessage(severity, message, message));
	}

}


	public static boolean isPostback() {
		return getFacesContext().isPostback();
	}


	public static String uploadDeArquivo(String pasta, UploadedFile uploadedFile) throws IOException {
		System.out.println("upload de arquivo -> " + filePath(pasta, uploadedFile.getFileName()));
		File file = new File(filePath(pasta, uploadedFile.getFileName()));
		if (!file.exists() && !new File(folderPath(pasta)).exists()) {
			if (!new File(folderPath(pasta)).mkdirs()) {
				throw new RuntimeException("Não foi possível criar o diretório solicitado!");
			} else {
				System.out.println("Criando arquivo...");
				file.createNewFile();
			}
		}
		FileOutputStream out = new FileOutputStream(file);
		out.write(uploadedFile.getContents());
		out.close();
		return file.getPath();
	}

	public static String uploadDeArquivo(String pasta, UploadedFile uploadedFile, String nome) throws IOException {
		System.out.println("upload de arquivo -> " + filePath(pasta, nome));
		File file = new File(filePath(pasta, nome));
		if (!file.exists() && !new File(folderPath(pasta)).exists()) {
			if (!new File(folderPath(pasta)).mkdirs()) {
				throw new RuntimeException("Não foi possível criar o diretório solicitado!");
			} else {
				System.out.println("Criando arquivo...");
				file.createNewFile();
			}
		}
		FileOutputStream out = new FileOutputStream(file);
		out.write(uploadedFile.getContents());
		out.close();
		return file.getPath();
	}

	public static String folderPath(String pasta) {
		return getRealPah("") + "WEB-INF" + MyUtil.separator + pasta;
	}

	public static String filePath(String pasta, String filename) {
		return getRealPah("") + "WEB-INF" + MyUtil.separator + pasta + MyUtil.separator + filename;
	}

	public static void adicionarMsgSave() {
		getFacesContext().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Operação Concluída", "Registro Salvo com sucesso!"));
	
	public static void adicionarMsgUpdate() {
		getFacesContext().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Operação Concluída", "Registro Atualizado com sucesso!"));
	}
}

	public static void adicionarMsgDelete() {
		getFacesContext().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Operação Concluída", "Registro Excluido com sucesso!"));
	}

	public static void adicionarMsgError(String msgError) {
		getFacesContext().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, msgError, ""));
	}

	public static void adicionarMsgInfo(String msgInfo) {
		getFacesContext().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, msgInfo, "Informação"));
	}

	public static HttpServletResponse getResponse() {
		return (HttpServletResponse) getFacesContext().getExternalContext().getResponse();
	}

	public static HttpServletRequest getRequest() {
		return (HttpServletRequest) getFacesContext().getExternalContext().getRequest();
	}

	public static void msgInDialog(Severity severity, String summary, String detail) {
		PrimeFaces.current().dialog().showMessageDynamic(new FacesMessage(severity, summary, detail));
	}

	public static void closeDialog(Object obj) {
		PrimeFaces.current().dialog().closeDynamic(obj);
	}

	public static void showXhtmlDialog(String widgetVar) {
		PrimeFaces.current().executeScript("PF('" + widgetVar + "').show();");
	}

	public static void openCurrentDialog(String outcome, Map<String, Object> options,
			Map<String, List<String>> parametros) {
		PrimeFaces.current().dialog().openDynamic(outcome, options, parametros);
	}

	public static void msgInDialogSucesso(String titulo, String msg) {
		PrimeFaces.current().dialog().showMessageDynamic(new FacesMessage(FacesMessage.SEVERITY_INFO, titulo, msg));
	}

	public static void msgInDialogError(String titulo, String msg) {
		PrimeFaces.current().dialog().showMessageDynamic(new FacesMessage(FacesMessage.SEVERITY_FATAL, titulo, msg));
	}

	public static Object getFlash(String objeto) throws ClassCastException {
		return getFacesContext().getExternalContext().getFlash().get(objeto);
	}

	public static Object putFlash(String alias, Object objeto) throws ClassCastException {
		return getFacesContext().getExternalContext().getFlash().put(alias, objeto);
	}

	public static String getParam(String param) {
		return getFacesContext().getExternalContext().getRequestParameterMap().get(param);
	}

	public static String putParam(String name, String param) {
		return getFacesContext().getExternalContext().getRequestParameterMap().put(name, param);
	}

	public static String getBrowser() {
		return getFacesContext().getExternalContext().getRequestHeaderMap().get("User-Agent");
	}

	public static FacesContext getContext() {
		return getFacesContext();
	}

	public static String getRealPah(String path) {
		return getFacesContext().getExternalContext().getRealPath(path);
	}

	public static FacesContext getFacesContext() {
		return FacesContext.getCurrentInstance();
	}

	public static ExternalContext getExternalContext() {
		return getFacesContext().getExternalContext();
	}

	public static String getContextPath() {
		return getFacesContext().getExternalContext().getRequestContextPath();
	}

	public static void redirect(String url) throws IOException {
		getFacesContext().getExternalContext().redirect(FacesUtil.getContextPath() + url);
	}

	public static void redirectSemContext(String url) throws IOException {
		getFacesContext().getExternalContext().redirect(url);
	}

	public static String getPaginaAtual() {
		return getFacesContext().getViewRoot().getViewId();
	}

	public static FacesContext getFacesContext(ServletRequest request, ServletResponse response) {
		FacesContext facesContext = getFacesContext();
		if (facesContext == null) {
			LifecycleFactory lifecycleFactory = (LifecycleFactory) FactoryFinder
					.getFactory(FactoryFinder.LIFECYCLE_FACTORY);
			Lifecycle lifecycle = lifecycleFactory.getLifecycle(LifecycleFactory.DEFAULT_LIFECYCLE);
			FacesContextFactory facesContextFactory = (FacesContextFactory) FactoryFinder
					.getFactory(FactoryFinder.FACES_CONTEXT_FACTORY);
			facesContext = facesContextFactory.getFacesContext(request.getServletContext(), request, response,
					lifecycle);
			facesContext.setViewRoot(facesContext.getApplication().getViewHandler().createView(facesContext, ""));
			FacesContextWrapper.setCurrentInstance(facesContext);
		}
		return facesContext;
	}

	public static abstract class FacesContextWrapper extends FacesContext {
		public static void setCurrentInstance(FacesContext facesContext) {
			FacesContext.setCurrentInstance(facesContext);
		}

	}

}
