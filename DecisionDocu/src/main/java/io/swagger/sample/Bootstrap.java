package io.swagger.sample;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import io.swagger.jaxrs.config.BeanConfig;

public class Bootstrap extends HttpServlet {
	private static final long serialVersionUID = 7938583012105290318L;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		
		BeanConfig beanConfig = new BeanConfig();
		beanConfig.setVersion("0.0.1");
		beanConfig.setTitle("DecisionDocu");
		beanConfig.setDescription("API Backend for decision-related management");
		beanConfig.setSchemes(new String[]{"http"});
		beanConfig.setHost("localhost:8080");
        beanConfig.setBasePath("/DecisionDocu/api");
        beanConfig.setContact("Benjamin Mayer, Martin Pfoser");
        beanConfig.setResourcePackage("at.jku.se.rest.api");
        beanConfig.setScan(true);
	}
}
