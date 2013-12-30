package org.wallride.admin.web.banner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.FlashMap;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.wallride.admin.service.BannerService;
import org.wallride.admin.support.AuthorizedUser;
import org.wallride.core.domain.Banner;
import org.wallride.core.web.DomainObjectDeletedModel;
import org.wallride.core.web.RestValidationErrorModel;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

@Controller
public class BannerRestController {

	@Inject
	private BannerService bannerService;

	@Inject
	private MessageSourceAccessor messageSourceAccessor;

	private static Logger logger = LoggerFactory.getLogger(BannerRestController.class);

	@ExceptionHandler(BindException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public @ResponseBody
	RestValidationErrorModel bindException(BindException e) {
		logger.debug("BindException", e);
		return RestValidationErrorModel.fromBindingResult(e.getBindingResult(), messageSourceAccessor);
	}

/*	@RequestMapping(value="/{language}/banners", method= RequestMethod.GET)
	public @ResponseBody BannerIndexModel index(@PathVariable String language) {
		BannerTree bannerTree = bannerService.readBannerTree(language);
		return new BannerIndexModel(bannerTree);
	}

//	@RequestMapping(value="/{language}/banners/{id}", method= RequestMethod.GET)
//	public void describe() {
//
//	}

	@RequestMapping(value="/{language}/banners", method=RequestMethod.POST)
	public @ResponseBody DomainObjectSavedModel save(
			@Valid BannerCreateForm form,
			BindingResult result,
			AuthorizedUser authorizedUser,
			HttpServletRequest request,
			HttpServletResponse response) throws BindException {
		if (result.hasErrors()) {
			throw new BindException(result);
		}
		Banner banner = bannerService.createBanner(form, result, authorizedUser);
		FlashMap flashMap = RequestContextUtils.getOutputFlashMap(request);
		flashMap.put("savedBanner", banner);
		RequestContextUtils.getFlashMapManager(request).saveOutputFlashMap(flashMap, request, response);
		return new DomainObjectSavedModel<Long>(banner);
	}

	@RequestMapping(value="/{language}/banners/{id}", method=RequestMethod.POST)
	public @ResponseBody DomainObjectUpdatedModel update(
			@Valid BannerEditForm form,
			BindingResult result,
			@PathVariable long id,
			AuthorizedUser authorizedUser,
			HttpServletRequest request,
			HttpServletResponse response) throws BindException {
		form.setId(id);
		if (result.hasErrors()) {
			throw new BindException(result);
		}
		Banner banner = bannerService.updateBanner(form, result, authorizedUser);
		FlashMap flashMap = RequestContextUtils.getOutputFlashMap(request);
		flashMap.put("updatedBanner", banner);
		RequestContextUtils.getFlashMapManager(request).saveOutputFlashMap(flashMap, request, response);
		return new DomainObjectUpdatedModel<Long>(banner);
	}
*/
	@RequestMapping(value="/{language}/banners/{id}", method= RequestMethod.DELETE)
	public @ResponseBody
	DomainObjectDeletedModel<Long> delete(
			@PathVariable String language,
			@PathVariable long id,
			AuthorizedUser authorizedUser,
			HttpServletRequest request,
			HttpServletResponse response) throws BindException {
		Banner banner = bannerService.deleteBanner(id, language);
		FlashMap flashMap = RequestContextUtils.getOutputFlashMap(request);
		flashMap.put("deletedBanner", banner);
		RequestContextUtils.getFlashMapManager(request).saveOutputFlashMap(flashMap, request, response);
		return new DomainObjectDeletedModel<Long>(banner);
	}

	@RequestMapping(value="/{language}/banners/{type}", method= RequestMethod.PUT, consumes= MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody
	BannerIndexModel sort(
			@PathVariable String language,
			@PathVariable Banner.Type type,
			@RequestBody List<Map<String, Object>> data) {
		bannerService.updateBannerSort(data, language);

		List<Banner> banners = bannerService.readBannersByType(type, language);
		return new BannerIndexModel(banners);
	}
}
