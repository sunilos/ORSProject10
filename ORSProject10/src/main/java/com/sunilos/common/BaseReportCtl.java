package com.sunilos.common;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.export.SimpleDocxReportConfiguration;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;

/**
 * Adds a generic list-report endpoint (PDF/DOCX via JasperReports) to every
 * controller that extends it, alongside the CRUD endpoints already inherited
 * from {@link BaseCtl}.
 * <p>
 * The JRXML template is located by convention from the DTO class name, so
 * subclasses need no extra code: {@code CourseDTO} resolves to classpath
 * resource {@code /reports/CourseListReport.jrxml}. Override
 * {@link #getReportView()} if a controller's template doesn't follow that
 * convention.
 * <p>
 * {@code GET /{entity}/report} streams an inline PDF (default) or, with
 * {@code ?type=doc}, an OOXML Word document, built from every record visible
 * to the logged-in user's organization.
 *
 * @param <F>
 * @param <T>
 * @param <S>
 */
public abstract class BaseReportCtl<F extends BaseForm, T extends BaseDTO, S extends BaseServiceInt<T>>
		extends BaseCtl<F, T, S> {

	private static final Logger log = LoggerFactory.getLogger(BaseReportCtl.class);

	/** Output format constant for PDF reports. */
	public static final String PDF = "pdf";

	/** Output format constant for Word (OOXML .docx) reports. */
	public static final String DOC = "doc";

	private static final Map<String, JasperReport> COMPILED_REPORT_CACHE = new ConcurrentHashMap<>();

	/**
	 * Classpath-relative path to this controller's JRXML template.
	 *
	 * @return non-null path to the JRXML resource
	 */
	protected String getReportView() {
		return "/reports/" + reportName() + "ListReport.jrxml";
	}

	private String reportName() {
		String dtoName = newDto().getClass().getSimpleName();
		return dtoName.endsWith("DTO") ? dtoName.substring(0, dtoName.length() - 3) : dtoName;
	}

	/**
	 * Returns the compiled {@link JasperReport} for the given JRXML path,
	 * compiling it on first use and caching the result for the lifetime of the
	 * application.
	 *
	 * @param reportTemplatePath classpath-relative path to the JRXML file
	 * @return compiled report ready for filling
	 * @throws IllegalStateException if the JRXML resource cannot be located
	 * @throws Exception             if JasperReports compilation fails
	 */
	private JasperReport getCompiledReport(String reportTemplatePath) throws Exception {
		JasperReport cached = COMPILED_REPORT_CACHE.get(reportTemplatePath);
		if (cached != null) {
			return cached;
		}
		synchronized (COMPILED_REPORT_CACHE) {
			cached = COMPILED_REPORT_CACHE.get(reportTemplatePath);
			if (cached != null) {
				return cached;
			}
			try (InputStream jrxml = getClass().getResourceAsStream(reportTemplatePath)) {
				if (jrxml == null) {
					throw new IllegalStateException("Report template not found: " + reportTemplatePath);
				}
				JasperReport compiled = JasperCompileManager.compileReport(jrxml);
				COMPILED_REPORT_CACHE.put(reportTemplatePath, compiled);
				return compiled;
			}
		}
	}

	/**
	 * Streams a list report of every record visible to the logged-in user's
	 * organization, as PDF (default) or DOCX ({@code ?type=doc}).
	 *
	 * @param type     {@value #PDF} or {@value #DOC}
	 * @param response the HTTP response the report is written to
	 */
	@PostMapping("/report")
	public void report(@RequestBody Map<String, Object> map, HttpServletResponse response) {
		try {

			String type = (String) map.get("reportType");

			List<?> list = baseService.search(newDto(), userContext);

			JasperReport jasperReport = getCompiledReport(getReportView());
			JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(list);
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, new HashMap<>(), dataSource);

			if (DOC.equals(type)) {
				response.setContentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
				response.setHeader("Content-Disposition", "inline; filename=\"" + reportName() + ".docx\"");
				JRDocxExporter exporter = new JRDocxExporter();
				exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
				exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(response.getOutputStream()));
				exporter.setConfiguration(new SimpleDocxReportConfiguration());
				exporter.exportReport();
			} else {
				response.setContentType("application/pdf");
				response.setHeader("Content-Disposition", "inline; filename=\"" + reportName() + ".pdf\"");
				JasperExportManager.exportReportToPdfStream(jasperPrint, response.getOutputStream());
			}
		} catch (Exception e) {
			log.error("{} report generation failed", getClass().getSimpleName(), e);
			try {
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
						"Failed to generate report: " + e.getMessage());
			} catch (IOException ignored) {
			}
		}
	}

}
