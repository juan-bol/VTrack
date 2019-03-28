package co.com.tracert.vtrack.web.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import co.com.tracert.vtrack.model.entities.Usuario;
import co.com.tracert.vtrack.model.constants.ConstantesVtrack;

/**
 * Servlet Filter implementation class VtrackFilter
 */
@WebFilter("/paginas/*")
public class VtrackFilter implements Filter {

	/**
	 * Default constructor.
	 */
	public VtrackFilter() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		// Obtener el usuario de la sesión
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		HttpSession hs = httpServletRequest.getSession();
		Usuario usuario = (Usuario) hs.getAttribute(ConstantesVtrack.USUARIO);

		// Verificar nulidad
		if (usuario == null) {
			// Se invalida la sesión
			hs.invalidate();
			// Si es null --> redirigir al login
			String url = ConstantesVtrack.PAGINA_LOGIN;
			HttpServletResponse httpServletResponse = (HttpServletResponse) response;
			//Redirige a la página de inicio login
			httpServletResponse.getWriter().write("<script>");
			httpServletResponse.getWriter().write("parent.location.replace('" + url + "');");
			httpServletResponse.getWriter().write("</script>");
		} else {
			// Si no
			chain.doFilter(request, response);
		}

	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
