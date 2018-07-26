package com.somnus.smart.biz.accquery.resource.impl;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import com.somnus.smart.biz.accquery.resource.AccQueryResource;
import com.somnus.smart.biz.accquery.service.AccQueryService;
import com.somnus.smart.message.accquery.CussubQueryRequest;
import com.somnus.smart.message.accquery.CussubQueryResponse;
import com.somnus.smart.message.accquery.IssuedAccDetailQueryRequest;
import com.somnus.smart.message.accquery.IssuedAccDetailQueryResponse;
import com.somnus.smart.message.accquery.IssuedAccQueryRequest;
import com.somnus.smart.message.accquery.IssuedAccQueryResponse;
import com.somnus.smart.message.accquery.QueryAccDetailRequest;
import com.somnus.smart.message.accquery.QueryAccDetailResponse;
import com.somnus.smart.message.accquery.QueryCusFifRequest;
import com.somnus.smart.message.accquery.QueryCusFifResponse;
import com.somnus.smart.message.accquery.QueryCusSubAccRequest;
import com.somnus.smart.message.accquery.QueryCusSubResponse;
import com.somnus.smart.message.accquery.QueryPayAppRequest;
import com.somnus.smart.message.accquery.QueryPayAppResponse;
import com.somnus.smart.service.common.Constants;
import com.somnus.smart.service.common.MessageUtil;
import com.somnus.smart.support.common.JsonUtils;
import com.somnus.smart.support.exceptions.BizException;

/**
 * 
 * @description: 账户查询接口
 * @author Somnus date 2015年3月6日 下午6:13:10
 */
@Component
@Validated
public class AccQueryResourceImpl implements AccQueryResource {

	private transient Logger log = LoggerFactory.getLogger(this.getClass());
	/** accQueryService */
	@Resource
	private AccQueryService accQueryService;

	public CussubQueryResponse queryCusCurBal(CussubQueryRequest request) {
		log.info(Constants.REQUEST_MSG, JsonUtils.toString(request));
		CussubQueryResponse response = new CussubQueryResponse();
		try {
			response = accQueryService.queryCusCurBal(request);
			// 返回成功报文
			MessageUtil.createCommMsg(response);
		} catch (BizException e) {
			log.error(Constants.BUSINESS_ERROR, e);
			// 组织错误报文
			MessageUtil.errRetrunInAction(response,e);
		} catch (Exception ex) {
			log.error(Constants.EXCEPTION_ERROR, ex);
			// 组织错误报文
			MessageUtil.createErrorMsg(response);
		}
		log.info(Constants.REPONSE_MSG, JsonUtils.toString(response));
		return response;
	}

	public IssuedAccQueryResponse queryIssuedAcc(IssuedAccQueryRequest request) {
		log.info(Constants.REQUEST_MSG, JsonUtils.toString(request));
		IssuedAccQueryResponse response = new IssuedAccQueryResponse();
		try {
			response = accQueryService.queryIssuedAcc(request);
			// 返回成功报文
			MessageUtil.createCommMsg(response);
		} catch (BizException e) {
			log.error(Constants.BUSINESS_ERROR, e);
			// 组织错误报文
			MessageUtil.errRetrunInAction(response,e);
		} catch (Exception ex) {
			log.error(Constants.EXCEPTION_ERROR, ex);
			// 组织错误报文
			MessageUtil.createErrorMsg(response);
		}
		log.info(Constants.REPONSE_MSG, JsonUtils.toString(response));
		return response;
	}

	public IssuedAccDetailQueryResponse queryIssuedAccDetail(IssuedAccDetailQueryRequest request) {
		log.info(Constants.REQUEST_MSG, JsonUtils.toString(request));
		IssuedAccDetailQueryResponse response = new IssuedAccDetailQueryResponse();
		try {
			response = accQueryService.queryIssuedAccDetail(request);
			// 返回成功报文
			MessageUtil.createCommMsg(response);
		} catch (BizException e) {
			log.error(Constants.BUSINESS_ERROR, e);
			// 组织错误报文
			MessageUtil.errRetrunInAction(response,e);
		} catch (Exception ex) {
			log.error(Constants.EXCEPTION_ERROR, ex);
			// 组织错误报文
			MessageUtil.createErrorMsg(response);
		}
		log.info(Constants.REPONSE_MSG, JsonUtils.toString(response));
		return response;
	}

	public QueryCusSubResponse queryCusSubAccInfo(QueryCusSubAccRequest request) {
		log.info(Constants.REQUEST_MSG, JsonUtils.toString(request));
		QueryCusSubResponse response = new QueryCusSubResponse();
		try {
			response = accQueryService.queryCusSubAccInfo(request);
			// 返回成功报文
			MessageUtil.createCommMsg(response);
		} catch (BizException e) {
			log.error(Constants.BUSINESS_ERROR, e);
			// 组织错误报文
			MessageUtil.errRetrunInAction(response,e);
		} catch (Exception ex) {
			log.error(Constants.EXCEPTION_ERROR, ex);
			// 组织错误报文
			MessageUtil.createErrorMsg(response);
		}
		log.info(Constants.REPONSE_MSG, JsonUtils.toString(response));
		return response;
	}

	public QueryAccDetailResponse queryAccDetail(QueryAccDetailRequest request) {
		log.info(Constants.REQUEST_MSG, JsonUtils.toString(request));
		QueryAccDetailResponse response = new QueryAccDetailResponse();
		try {
			response = accQueryService.queryAccDetail(request);
			// 返回成功报文
			MessageUtil.createCommMsg(response);
		} catch (BizException e) {
			log.error(Constants.BUSINESS_ERROR, e);
			// 组织错误报文
			MessageUtil.errRetrunInAction(response,e);
		} catch (Exception ex) {
			log.error(Constants.EXCEPTION_ERROR, ex);
			// 组织错误报文
			MessageUtil.createErrorMsg(response);
		}
		log.info(Constants.REPONSE_MSG, JsonUtils.toString(response));
		return response;
	}

	public QueryCusFifResponse queryCusFif(QueryCusFifRequest request) {
		log.info(Constants.REQUEST_MSG, JsonUtils.toString(request));
		QueryCusFifResponse response = new QueryCusFifResponse();
		try {
			response = accQueryService.queryCusFif(request);
			// 返回成功报文
			MessageUtil.createCommMsg(response);
		} catch (BizException e) {
			log.error(Constants.BUSINESS_ERROR, e);
			// 组织错误报文
			MessageUtil.errRetrunInAction(response,e);
		} catch (Exception ex) {
			log.error(Constants.EXCEPTION_ERROR, ex);
			// 组织错误报文
			MessageUtil.createErrorMsg();
		}
		log.info(Constants.REPONSE_MSG, JsonUtils.toString(response));
		return response;
	}

	public QueryPayAppResponse queryPaymentApp(QueryPayAppRequest request) {
		log.info(Constants.REQUEST_MSG, JsonUtils.toString(request));
		QueryPayAppResponse response = new QueryPayAppResponse();
		try {
			response = accQueryService.queryPaymentApp(request);
			// 返回成功报文
			MessageUtil.createCommMsg(response);
		} catch (BizException e) {
			log.error(Constants.BUSINESS_ERROR, e);
			// 组织错误报文
			 MessageUtil.errRetrunInAction(response,e);
		} catch (Exception ex) {
			log.error(Constants.EXCEPTION_ERROR, ex);
			// 组织错误报文
			MessageUtil.createErrorMsg(response);
		}
		log.info(Constants.REPONSE_MSG, JsonUtils.toString(response));
		return response;
	}

}
