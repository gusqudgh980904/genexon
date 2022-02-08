package kr.co.gnx.insa;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import kr.co.gnx.base.BaseController;
import kr.co.gnx.comm.excel.excelHandler2;
import kr.co.gnx.comm.util.CommUtil;
import kr.co.gnx.comm.util.session.SessionUtil;
import kr.co.gnx.insa.guarantee.GuaranteeVO;
import kr.co.gnx.security.model.User;
import kr.co.gnx.system.file.FileVO;
import kr.co.gnx.system.role.RoleVO;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller(value="InsaController")
public class InsaController extends BaseController {
	private static final Logger logger = LoggerFactory.getLogger(InsaController.class);
	
	/**
	 * Hyun ByeonHO
	 * 인사관리 테스트 페이지 이동
	 * @param requset
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/insa/testInsa.go")
	public ModelAndView testInsa(HttpServletRequest requset,HttpServletResponse response) {
		
		ModelAndView mv=new ModelAndView("/insa/insa/testInsa");
		
		return mv;
	}//testInsa
	
	/**
	 * Hyun ByeonHO
	 * 테스트 인사조회 화면
	 * @param requset
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/insa/getTestInsaList.ajax")
	public ModelAndView getTestInsaList(HttpServletRequest request,HttpServletResponse response,InsaVO insaVO) {
		ModelAndView mv = new ModelAndView("jsonView");
		User UserSession = SessionUtil.getSessionVO(request.getSession()).getUser();
		
		
		
		insaVO.setPage(insaVO.getPage());
		insaVO.setPageSize(insaVO.getPageSize());
		 insaVO.setMb_id(UserSession.getMB_ID());
		 /* insaVO.setIn_emp_cd(UserSession.getEMP_CD());
		 * insaVO.setUser_scd(UserSession.getSCD());
		 */
		
		mv.addObject("results", getInsaService().getTestInsaList(insaVO));
		
		return mv;
//		ModelAndView mv = new ModelAndView("jsonView");
//		User UserSession = SessionUtil.getSessionVO(request.getSession()).getUser();
//		
//		InsaVO insavo2 = new InsaVO();
//		
//		if(insaVO.getJSON_STRING() == null){
//			insavo2 = insaVO;
//		} else {
//			JSONObject paramJObj = JSONObject.fromObject(insaVO.getJSON_STRING());
//			insavo2 = (InsaVO) JSONObject.toBean(paramJObj, InsaVO.class);
//		}
//		
//		insavo2.setPage(insaVO.getPage());
//		insavo2.setPageSize(insaVO.getPageSize());
//		insavo2.setMb_id(UserSession.getMB_ID());
//		insavo2.setIn_emp_cd(UserSession.getEMP_CD());
//		insavo2.setUser_scd(UserSession.getSCD());
//		insavo2.setView_auth((String)request.getAttribute("view_auth"));
//		
//		mv.addObject("results", getInsaService().getTestInsaList(insavo2));
//
//		return mv;
	}//getTestInsaList
	
	
	/**
	 * @Description  : 테스트엑셀다운 현병호
	 * @author       : lakhyun.kim
	 * @since        : 2018. 06. 04
	 * @return       : void
	 */
	@RequestMapping(value = "/insa/getInsaTestListExcel.ajax")
	public void getInsaTestListExcel(HttpServletRequest request, HttpServletResponse response, InsaVO insaVO) throws Exception{
		User UserSession = SessionUtil.getSessionVO(request.getSession()).getUser();
		insaVO.setMb_id(UserSession.getMB_ID());
		insaVO.setIn_emp_cd(UserSession.getEMP_CD());
		insaVO.setUser_scd(UserSession.getSCD());
		insaVO.setView_auth((String)request.getAttribute("view_auth"));
		insaVO.setRole_id(UserSession.getROLE_ID());
		excelHandler2 eh =  getInsaService().getInsaTestListExcel(insaVO);

		if (eh.getRowindex() == 0) {
			CommUtil.sendGenexonAlert(response, "info", "인사정보 - 엑셀다운로드", "조회 데이타가 없습니다.");
		} else {
			String filename = "인사정보_" + CommUtil.getCurrentDateTime() + ".xlsx";
			eh.sendResponse(response, filename);
		}
	}
	
	/**
	 * @Description  : 등록관련 사원찾기팝업
	 * @author       : lakhyun.kim
	 * @since        : 2018. 05. 25
	 * @return       : ModelAndView
	 */
	@RequestMapping(value = "/insa/popup/empSrchMiniPop.pop")
	public ModelAndView empSchPop3(HttpServletRequest request, HttpServletResponse response, InsaVO insaVO) throws Exception{
		ModelAndView mv = new ModelAndView("/insa/insa/empSrchMiniPop");
		mv.addObject("InsaVO", insaVO);
		return mv;
	}
	
	/**
	 * @Description  : 등록관련 분할조직장찾기팝업
	 * @author       : lakhyun.kim
	 * @since        : 2018. 05. 25
	 * @return       : ModelAndView
	 */
	@RequestMapping(value = "/insa/popup/dvdEmpSrchMiniPop.pop")
	public ModelAndView dvdEmpSrchMiniPop(HttpServletRequest request, HttpServletResponse response, InsaVO insaVO) throws Exception{
		ModelAndView mv = new ModelAndView("/insa/insa/empSrchMiniPop");
		mv.addObject("InsaVO", insaVO);
		return mv;
	}
	
	/**
	 * @Description  : 부모창 크기에 영향받지 않는 조직찾기 팝업
	 * @author       : lakhyun.kim
	 * @since        : 2019. 03. 15
	 * @return       : ModelAndView
	 */
	@RequestMapping(value = "/insa/popup/empSrchMiniPop2.pop")
	public ModelAndView empSrchMiniPop2(HttpServletRequest request, HttpServletResponse response, InsaVO insaVO) throws Exception{
		ModelAndView mv = new ModelAndView("/insa/insa/empSrchMiniPop2");
		mv.addObject("InsaVO", insaVO);
		return mv;
	}
	
	/**
	 * @Description  : 등록관련 사원찾기팝업 사원조회
	 * @author       : lakhyun.kim
	 * @since        : 2018. 05. 25
	 * @return       : ModelAndView
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/insa/getEmpSchList.ajax")
	public ModelAndView getEmpSchList(HttpServletRequest request, HttpServletResponse response, InsaVO insaVO) throws Exception{
		ModelAndView mv = new ModelAndView("jsonView");
		User UserSession = SessionUtil.getSessionVO(request.getSession()).getUser();
		insaVO.setMb_id(UserSession.getMB_ID());
		insaVO.setIn_emp_cd(UserSession.getEMP_CD());
		insaVO.setUser_scd(UserSession.getSCD());
		
		Map<String, String> menuRole = (Map<String, String>)request.getAttribute("menuRole");
		
		if(CommUtil.isNotEmpty(menuRole)) {
			insaVO.setView_auth(menuRole.get("view_auth"));
		}
		
		mv.addObject("results", getInsaService().getEmpSchList(insaVO));
		return mv;
	}

	/**
	 * @Description  : 권한사원변경 팝업
	 * @author       : lakhyun.kim
	 * @since        : 2019. 03. 07
	 * @return       : ModelAndView
	 */
	@RequestMapping(value = "/insa/popup/empSrchRolePop.pop")
	public ModelAndView empSrchRolePop(HttpServletRequest request, HttpServletResponse response, InsaVO insaVO) throws Exception{
		ModelAndView mv = new ModelAndView("/insa/insa/empSrchRolePop");
		mv.addObject("InsaVO", insaVO);
		return mv;
	}

	/**
	 * @Description  : 권한사원변경 팝업 사원조회
	 * @author       : lakhyun.kim
	 * @since        : 2019. 03. 27
	 * @return       : ModelAndView
	 */
	@RequestMapping(value = "/insa/getEmpSrchRoleList.ajax")
	public ModelAndView getEmpSrchRoleList(HttpServletRequest request, HttpServletResponse response, InsaVO insaVO) throws Exception{
		ModelAndView mv = new ModelAndView("jsonView");
		User UserSession = SessionUtil.getSessionVO(request.getSession()).getUser();
		insaVO.setMb_id(UserSession.getMB_ID());
		insaVO.setIn_emp_cd(UserSession.getEMP_CD());
		mv.addObject("results", getInsaService().getEmpSrchRoleList(insaVO));
		return mv;
	}
	
	/**
	 * @Description  : 인사정보 페이지 이동
	 * @author       : lakhyun.kim
	 * @since        : 2018. 05. 29
	 * @return       : ModelAndView
	 */
	@RequestMapping(value = "/insa/insa/insa.go")
	public ModelAndView insa(HttpServletRequest request, HttpServletResponse response, InsaVO insaVO) throws Exception{
		String PATH ="/insa/insa/insa";
		if(insaVO.getMenu_view_type()!=null && "TYPE2".equals(insaVO.getMenu_view_type())) {
			PATH ="/insa/insa/insaType2";
		}else if(insaVO.getMenu_view_type()!=null && "TYPE11".equals(insaVO.getMenu_view_type())) {
			PATH = "/insa/insa/insaType11";
		}else if(insaVO.getMenu_view_type()!=null && "TYPE3".equals(insaVO.getMenu_view_type())) { //NIKE
			PATH = "/insa/insa/insaType3";
		}

		ModelAndView mv = new ModelAndView(PATH);

		User UserSession = SessionUtil.getSessionVO(request.getSession()).getUser();
		Map<String, String> tempmap = new HashMap<String, String>();
		
		// 어센틱 인사정보 조회시 사원검색(팝업) 권한 사용
		RoleVO roleVO = new RoleVO();
		roleVO.setMb_id(UserSession.getMB_ID());
		roleVO.setRole_id(UserSession.getROLE_ID());
		roleVO.setSEARCH_WORD("/insa/getEmpSchList.ajax");
		mv.addObject("RoleResult", getRoleService().getMenuRoleList(roleVO));
		
		tempmap.put("ROLE_ID", UserSession.getROLE_ID());
		tempmap.put("MB_ID", UserSession.getMB_ID());
		mv.addObject("User", tempmap);
		
		mv.addObject("InsaVO", insaVO);
		return mv;
	}
	
	/**
	 * @brief 인사정보 조회
	 * @param request
	 * @param response
	 * @param InSaVO
	 * @return ModelAndView
	 */
	@RequestMapping(value = "/insa/getInsaList.ajax")
	public ModelAndView getInsaList(HttpServletRequest request, HttpServletResponse response, InsaVO insaVO) throws Exception{
		ModelAndView mv = new ModelAndView("jsonView");
		User UserSession = SessionUtil.getSessionVO(request.getSession()).getUser();
		
		InsaVO insavo2 = new InsaVO();
		
		if(insaVO.getJSON_STRING() == null){
			insavo2 = insaVO;
		} else {
			JSONObject paramJObj = JSONObject.fromObject(insaVO.getJSON_STRING());
			insavo2 = (InsaVO) JSONObject.toBean(paramJObj, InsaVO.class);
		}
		
		insavo2.setPage(insaVO.getPage());
		insavo2.setPageSize(insaVO.getPageSize());
		insavo2.setMb_id(UserSession.getMB_ID());
		insavo2.setIn_emp_cd(UserSession.getEMP_CD());
		insavo2.setUser_scd(UserSession.getSCD());
		insavo2.setView_auth((String)request.getAttribute("view_auth"));
		
		mv.addObject("results", getInsaService().getInsaList(insavo2));

		return mv;
	}
	
	/**
	 * @Description  : 인사 정보 엑셀다운로드
	 * @author       : lakhyun.kim
	 * @since        : 2018. 06. 04
	 * @return       : void
	 */
	@RequestMapping(value = "/insa/getInsaListExcel.ajax")
	public void getInsaListExcel(HttpServletRequest request, HttpServletResponse response, InsaVO insaVO) throws Exception{
		User UserSession = SessionUtil.getSessionVO(request.getSession()).getUser();
		insaVO.setMb_id(UserSession.getMB_ID());
		insaVO.setIn_emp_cd(UserSession.getEMP_CD());
		insaVO.setUser_scd(UserSession.getSCD());
		insaVO.setView_auth((String)request.getAttribute("view_auth"));
		insaVO.setRole_id(UserSession.getROLE_ID());
		excelHandler2 eh =  getInsaService().getInsaListExcel(insaVO);

		if (eh.getRowindex() == 0) {
			CommUtil.sendGenexonAlert(response, "info", "인사정보 - 엑셀다운로드", "조회 데이타가 없습니다.");
		} else {
			String filename = "인사정보_" + CommUtil.getCurrentDateTime() + ".xlsx";
			eh.sendResponse(response, filename);
		}
	}
	
	/**
	 * @Description  : 인사 등록/수정 팝업
	 * @author       : lakhyun.kim
	 * @since        : 2018. 05. 30
	 * @return       : ModelAndView
	 */
	@RequestMapping(value = "/insa/insa/insaRegi.pop")
	public ModelAndView insaRegi(HttpServletRequest request, HttpServletResponse response, InsaVO insaVO) throws Exception{
		String PATH = "/insa/insa/insaRegi";
		
		if(insaVO.getMenu_view_type()!=null && "TYPE11".equals(insaVO.getMenu_view_type())) {
			PATH = "/insa/insa/insaRegiType11";
		}else if (insaVO.getMenu_view_type()!=null && "TYPE3".equals(insaVO.getMenu_view_type())) { //NIKE
			PATH = "/insa/insa/insaRegiType3";
		}
		
		ModelAndView mv = new ModelAndView(PATH);
		
		InsaVO insaVO2 = new InsaVO();
		User UserSession = SessionUtil.getSessionVO(request.getSession()).getUser();
		JSONObject paramJObj = JSONObject.fromObject(insaVO.getJSON_STRING());
		insaVO2 = (InsaVO) JSONObject.toBean(paramJObj,InsaVO.class);
		insaVO2.setMb_id(UserSession.getMB_ID());
		if (!"".equals(insaVO2.getEmp_cd())) { // 수정
			mv.addObject("InsaVO", getInsaService().getEmpView(insaVO2));
			
			// 파일목록 조회
			FileVO fileVO = new FileVO();
			fileVO.setMb_id(UserSession.getMB_ID());
			fileVO.setRef_seq(insaVO2.getSeq());
			mv.addObject("fileList", JSONArray.fromObject(getFileService().getFileInfoList(fileVO)));
			
		} else { // 등록
			mv.addObject("InsaVO", insaVO2);
		}
		
		return mv;
	}
	
	/**
	 * @Description  : 인사 상세정보 팝업
	 * @author       : lakhyun.kim
	 * @since        : 2018. 07. 06
	 * @return       : String
	 */
	@RequestMapping(value = "/insa/insa/insaDetail.pop")
	public ModelAndView insaDetail(HttpServletRequest request, HttpServletResponse response, InsaVO insaVO) throws Exception{
		String PATH = "/insa/insa/insaDetail";
		User UserSession = SessionUtil.getSessionVO(request.getSession()).getUser();
		
		if(UserSession.getMB_ID()!=null && "HSH".equals(UserSession.getMB_ID())) {
			PATH = "/insa/insa/insaDetailType7";
		}

		ModelAndView mv = new ModelAndView(PATH);
		
		JSONObject paramJObj = JSONObject.fromObject(insaVO.getJSON_STRING());
		insaVO = (InsaVO) JSONObject.toBean(paramJObj,InsaVO.class);
		insaVO.setMb_id(UserSession.getMB_ID());
		// 인사정보
		mv.addObject("InsaVO", getInsaService().getEmpView(insaVO));
		Map<String, Object> tempmap = new HashMap<String, Object>();
		tempmap.put("ROLE_IDS", UserSession.getAuthorities());
		tempmap.put("MB_ID", UserSession.getMB_ID());
		tempmap.put("EMP_CD", UserSession.getEMP_CD());
		mv.addObject("User", tempmap);
		return mv;
	}
	
	/**
	 * @Description  : 인사 등록
	 * @author       : lakhyun.kim
	 * @since        : 2018. 05. 30
	 * @return       : ModelAndView
	 */
	@RequestMapping(value = "/insa/insertInsa.ajax")
	public ModelAndView insertInsa(MultipartHttpServletRequest request, InsaVO insaVO) throws Exception{
		ModelAndView mav = new ModelAndView("jsonView"); 
		User UserSession = SessionUtil.getSessionVO(request.getSession()).getUser();

		getInsaService().setUserSession(UserSession);
        mav.addObject("results", getInsaService().insertInsa(request, insaVO));
        return mav;
	}

	/**
	 * @Description  : 인사 수정
	 * @author       : lakhyun.kim
	 * @since        : 2018. 06. 04
	 * @return       : ModelAndView
	 */
	@RequestMapping(value = "/insa/updateInsa.ajax")
	public ModelAndView updateInsa(MultipartHttpServletRequest request, InsaVO insaVO) throws Exception{
		ModelAndView mav = new ModelAndView("jsonView"); 
		User UserSession = SessionUtil.getSessionVO(request.getSession()).getUser();
		getInsaService().setUserSession(UserSession);
        mav.addObject("results", getInsaService().updateInsa(request, insaVO));
	        
        return mav;
	}
	
	/**
	 * @Description  : 인사 삭제
	 * @author       : lakhyun.kim
	 * @since        : 2018. 06. 04
	 * @return       : ModelAndView
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/insa/deleteinsa.ajax")
	public ModelAndView deleteinsa(HttpServletRequest request, HttpServletResponse response, @RequestBody JSONObject paramJObj) throws Exception{
		ModelAndView mv = new ModelAndView("jsonView");
		ArrayList<InsaVO> models = (ArrayList<InsaVO>) JSONArray.toCollection(paramJObj.getJSONArray("models"), InsaVO.class);
		User UserSession = SessionUtil.getSessionVO(request.getSession()).getUser();
		getInsaService().setUserSession(UserSession);
		mv.addObject("results", getInsaService().deleteinsa(models.get(0)));

		return mv;
	}
	
	/*********************************************************** 인사마감 ****************************************************/
	/**
	 * @Description  : 인사마감 페이지 이동
	 * @author       : yumi.jeon
	 * @since        : 2019. 02. 27
	 * @return       : ModelAndView
	 */
	@RequestMapping(value = "/insa/insa/monthInsa.go")
	public ModelAndView monthInsa(HttpServletRequest request, HttpServletResponse response, InsaVO insaVO) throws Exception{
		String PATH = "/insa/insa/monthInsa";
		if(insaVO.getMenu_view_type() != null && "TYPE11".equals(insaVO.getMenu_view_type())) {
			PATH = "/insa/insa/monthInsaTYPE11";
		}
		
		ModelAndView mv = new ModelAndView(PATH);
		User UserSession = SessionUtil.getSessionVO(request.getSession()).getUser();
		Map<String, String> tempmap = new HashMap<String, String>();
		tempmap.put("ROLE_ID", UserSession.getROLE_ID());
		tempmap.put("MB_ID", UserSession.getMB_ID());
		mv.addObject("User", tempmap);
		return mv;
	}
	
	/**
	 * @Description  : 인사마감 리스트 조회
	 * @author       : yumi.jeon
	 * @since        : 2019. 02. 27
	 * @return ModelAndView
	 */
	@RequestMapping(value = "/insa/getMonthInsaList.ajax")
	public ModelAndView getMonthInsaList(HttpServletRequest request, HttpServletResponse response, InsaVO insaVO) throws Exception{
		ModelAndView mv = new ModelAndView("jsonView");
		User UserSession = SessionUtil.getSessionVO(request.getSession()).getUser();
		if(insaVO.getJSON_STRING() == null){
			InsaVO insavo2 = new InsaVO();
			insaVO = insavo2;
		} else {
			JSONObject paramJObj = JSONObject.fromObject(insaVO.getJSON_STRING());
			insaVO = (InsaVO) JSONObject.toBean(paramJObj,InsaVO.class);
		}
		insaVO.setMb_id(UserSession.getMB_ID());
		insaVO.setIn_emp_cd(UserSession.getEMP_CD());
		insaVO.setUser_scd(UserSession.getSCD());
		insaVO.setView_auth((String)request.getAttribute("view_auth"));
		mv.addObject("results", getInsaService().getMonthInsaList(insaVO));
		return mv;
	}
	
	/**
	 * @Description  : 인사마감 수정 페이지
	 * @author       : yumi.jeon
	 * @since        : 2019. 02. 27
	 * @return       : ModelAndView
	 */
	@RequestMapping(value = "/insa/insa/monthInsaRegi.pop")
	public ModelAndView monthInsaRegi(HttpServletRequest request, HttpServletResponse response, InsaVO insaVO) throws Exception{
		String PATH ="/insa/insa/monthInsaRegi";
		if(insaVO.getMenu_view_type() != null && "TYPE11".equals(insaVO.getMenu_view_type())) {
			PATH ="/insa/insa/monthInsaRegiTYPE11";
		}
		ModelAndView mv = new ModelAndView(PATH);
		InsaVO insaVO2 = new InsaVO();
		User UserSession = SessionUtil.getSessionVO(request.getSession()).getUser();
		JSONObject paramJObj = JSONObject.fromObject(insaVO.getJSON_STRING());
		insaVO2 = (InsaVO) JSONObject.toBean(paramJObj,InsaVO.class);
		insaVO2.setMb_id(UserSession.getMB_ID());
		if (!"".equals(insaVO2.getEmp_cd())) {
			mv.addObject("InsaVO", getInsaService().getMonthEmpView(insaVO2));
			
		} else { 
			mv.addObject("InsaVO", insaVO);
		}
		return mv;
	}
	
	/**
	 * @Description  : 인사 수정
	 * @author       : yumi.jeon
	 * @since        : 2019. 02. 27
	 * @return       : ModelAndView
	 */
	@RequestMapping(value = "/insa/updateMonthInsa.ajax")
	public ModelAndView updateMonthInsa(MultipartHttpServletRequest request, InsaVO insaVO) throws Exception{
		ModelAndView mav = new ModelAndView("jsonView"); 
		User UserSession = SessionUtil.getSessionVO(request.getSession()).getUser();
		getInsaService().setUserSession(UserSession);
        mav.addObject("results", getInsaService().updateMonthInsa(request, insaVO));
        return mav;
	}

	/**
	 * @Description  : 인사 상세정보 팝업
	 * @author       : yumi.jeon
	 * @since        : 2019. 02. 27
	 * @return       : String
	 */
	@RequestMapping(value = "/insa/insa/monthInsaDetail.pop")
	public ModelAndView monthInsaDetail(HttpServletRequest request, HttpServletResponse response, InsaVO insaVO) throws Exception{
		ModelAndView mv = new ModelAndView("/insa/insa/insaDetail");
		User UserSession = SessionUtil.getSessionVO(request.getSession()).getUser();
		JSONObject paramJObj = JSONObject.fromObject(insaVO.getJSON_STRING());
		insaVO = (InsaVO) JSONObject.toBean(paramJObj,InsaVO.class);
		insaVO.setMb_id(UserSession.getMB_ID());
		// 인사정보
		mv.addObject("InsaVO", getInsaService().getMonthEmpView(insaVO));
		return mv;
	}
	
	/**
	 * @Description  : 인사마감 정보 엑셀다운로드
	 * @author       : yumi.jeon
	 * @since        : 2019. 02. 27
	 * @return       : void
	 */
	@RequestMapping(value = "/insa/getMonthInsaListExcel.ajax")
	public void getMonthInsaListExcel(HttpServletRequest request, HttpServletResponse response, InsaVO insaVO) throws Exception{
		User UserSession = SessionUtil.getSessionVO(request.getSession()).getUser();
		insaVO.setMb_id(UserSession.getMB_ID());
		insaVO.setIn_emp_cd(UserSession.getEMP_CD());
		insaVO.setUser_scd(UserSession.getSCD());
		insaVO.setView_auth((String)request.getAttribute("view_auth"));
		excelHandler2 eh =  getInsaService().getMonthInsaListExcel(insaVO);

		if (eh.getRowindex() == 0) {
			CommUtil.sendGenexonAlert(response, "info", "인사마감정보 - 엑셀다운로드", "조회 데이타가 없습니다.");
		} else {
			String filename = "인사마감정보_" + CommUtil.getCurrentDateTime() + ".xlsx";
			eh.sendResponse(response, filename);
		}
	}
	
	/**
	 * @Description  : 인사마감 삭제
	 * @author       : lakhyun.kim
	 * @since        : 2018. 06. 04
	 * @return       : ModelAndView
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/insa/deleteMonthInsa.ajax")
	public ModelAndView deleteMonthInsa(HttpServletRequest request, HttpServletResponse response, @RequestBody JSONObject paramJObj) throws Exception{
		ModelAndView mv = new ModelAndView("jsonView");
		ArrayList<InsaVO> models = (ArrayList<InsaVO>) JSONArray.toCollection(paramJObj.getJSONArray("models"), InsaVO.class);
		User UserSession = SessionUtil.getSessionVO(request.getSession()).getUser();
		getInsaService().setUserSession(UserSession);
		mv.addObject("results", getInsaService().deleteMonthInsa(models.get(0)));

		return mv;
	}

	/**
	 * @Description  : 인사 사원 조회 DB분배 인사 대상자 기준  조회
	 * @author       : lakhyun.kim
	 * @since        : 2019. 04. 30
	 * @return       : ModelAndView
	 */
	@RequestMapping(value = "/insa/getEmpSchDbTargetList.ajax")
	public ModelAndView getEmpSchDbTargetList(HttpServletRequest request, HttpServletResponse response, InsaVO insaVO) throws Exception{
		ModelAndView mv = new ModelAndView("jsonView");
		User UserSession = SessionUtil.getSessionVO(request.getSession()).getUser();
		insaVO.setMb_id(UserSession.getMB_ID());
		
		insaVO.setIn_emp_cd(UserSession.getEMP_CD());
		if(CommUtil.isNotEmpty(insaVO.getJSON_STRING())) {
			JSONObject paramJObj = JSONObject.fromObject(insaVO.getJSON_STRING());
			insaVO = (InsaVO) JSONObject.toBean(paramJObj, InsaVO.class);
		}
		
		if(null!=insaVO.getSRCH_DBATTRIBUTE() && !"".equals(insaVO.getSRCH_DBATTRIBUTE())) {
			mv.addObject("results", getInsaService().getEmpSchDbTargetList(insaVO));
		}else {
			mv.addObject("results", new ArrayList<InsaVO>());
		}
		
		return mv;
	}
	
	/**
	 * @Description  : 인사 사원 조회 DB분배 동의 대상자 기준  조회
	 * @author       : lakhyun.kim
	 * @since        : 2019. 04. 30
	 * @return       : ModelAndView
	 */
	@RequestMapping(value = "/insa/getDbTargetSchList.ajax")
	public ModelAndView getDbTargetSchList(HttpServletRequest request, HttpServletResponse response, InsaVO insaVO) throws Exception{
		ModelAndView mv = new ModelAndView("jsonView");
		User userSession = SessionUtil.getSessionVO(request.getSession()).getUser();
		getInsaService().setUserSession(userSession);
		if(CommUtil.isNotEmpty(insaVO.getJSON_STRING())) {
			JSONObject paramJObj = JSONObject.fromObject(insaVO.getJSON_STRING());
			insaVO = (InsaVO) JSONObject.toBean(paramJObj, InsaVO.class);
		}
		if(null!=insaVO.getSRCH_DBATTRIBUTE() && !"".equals(insaVO.getSRCH_DBATTRIBUTE())) {
			mv.addObject("results", getInsaService().getDbTargetSchList(insaVO));
		}else {
			mv.addObject("results", new ArrayList<InsaVO>());
		}
		return mv;
	}
	
	/*********************************************************** 평가인사마감 ****************************************************/
	/**
	 * @Description  : 인사마감 페이지 이동
	 * @author       : yumi.jeon
	 * @since        : 2019. 02. 27
	 * @return       : ModelAndView
	 */
	@RequestMapping(value = "/insa/insa/evaluateMonthInsa.go")
	public ModelAndView evaluateMonthInsa(HttpServletRequest request, HttpServletResponse response) throws Exception{
		ModelAndView mv = new ModelAndView("/insa/insa/evaluateMonthInsa");
		User UserSession = SessionUtil.getSessionVO(request.getSession()).getUser();
		Map<String, String> tempmap = new HashMap<String, String>();
		tempmap.put("ROLE_ID", UserSession.getROLE_ID());
		mv.addObject("User", tempmap);
		return mv;
	}
	
	
	/**
	 * @Description  : 평가인사 마감 리스트 조회
	 * @author       : wonheyok.choi	
	 * @since        : 2020. 12. 04
	 * @return ModelAndView
	 */
	@RequestMapping(value = "/insa/insa/getMonthEvalInsaList.ajax")
	public ModelAndView getMonthEvalInsaList(HttpServletRequest request, HttpServletResponse response, InsaVO insaVO) throws Exception{
		ModelAndView mv = new ModelAndView("jsonView");
		User UserSession = SessionUtil.getSessionVO(request.getSession()).getUser();
		InsaVO insavo2 = new InsaVO();
		if(null != insaVO.getJSON_STRING()){
			JSONObject paramJObj = JSONObject.fromObject(insaVO.getJSON_STRING());
			insavo2 = (InsaVO) JSONObject.toBean(paramJObj,InsaVO.class);
		} 
		
		insavo2.setMb_id(UserSession.getMB_ID());
		insavo2.setIn_emp_cd(UserSession.getEMP_CD());
		insavo2.setUser_scd(UserSession.getSCD());
		insavo2.setPage(insaVO.getPage());
		insavo2.setPageSize(insaVO.getPageSize());
		insavo2.setView_auth((String)request.getAttribute("view_auth"));
		mv.addObject("results", getInsaService().getMonthEvalInsaList(insavo2));
		return mv;
	}
	
	/**
	 * @Description  : 평가인사 마감 리스트 엑셀다운로드
	 * @author       : wonheyok.choi	
	 * @since        : 2020. 12. 04
	 * @return       : void
	 */
	@RequestMapping(value = "/insa/insa/getMonthEvalInsaListExcel.ajax")
	public void getMonthEvalInsaListExcel(HttpServletRequest request, HttpServletResponse response, InsaVO insaVO) throws Exception{
		User UserSession = SessionUtil.getSessionVO(request.getSession()).getUser();
		insaVO.setMb_id(UserSession.getMB_ID());
		insaVO.setIn_emp_cd(UserSession.getEMP_CD());
		insaVO.setUser_scd(UserSession.getSCD());
		insaVO.setView_auth((String)request.getAttribute("view_auth"));
		excelHandler2 eh =  getInsaService().getMonthEvalInsaListExcel(insaVO);

		if (eh.getRowindex() == 0) {
			CommUtil.sendGenexonAlert(response, "info", "평가인사마감 - 엑셀다운로드", "조회 데이타가 없습니다.");
		} else {
			String filename = "평가인사마감_" + CommUtil.getCurrentDateTime() + ".xlsx";
			eh.sendResponse(response, filename);
		}
	}
	
	/**
	 * @Description  : 평가인사 마감 수정
	 * @author       : wonhyeok.choi
	 * @since        : 2020. 12. 11
	 * @return       : ModelAndView
	 */
	@RequestMapping(value = "/insa/insa/updateMonthEvalInsaList.ajax")
	public ModelAndView updateMonthEvalInsaList(MultipartHttpServletRequest request, InsaVO insaVO) throws Exception{
		ModelAndView mav = new ModelAndView("jsonView"); 
		User UserSession = SessionUtil.getSessionVO(request.getSession()).getUser();
		getInsaService().setUserSession(UserSession);
        mav.addObject("results", getInsaService().updateMonthEvalInsaList(request, insaVO));
	        
        return mav;
	}
	/*********************************************************** 평가인사마감 ****************************************************/
	
	/*********************************************************** HSH인사 상세정보 리쿠르팅 ****************************************************/
	/**
	 * @Description  : 인사 상세정보 리쿠르팅 가져오기
	 * @author       : bosung.kim
	 * @since        : 2020. 12. 15
	 * @return       : ModelAndView
	 */
	@RequestMapping(value = "/insa/insa/getRecruitList.ajax")
	public ModelAndView getRecruitList(HttpServletRequest request, HttpServletResponse response, InsaVO insaVO) throws Exception{
		ModelAndView mv = new ModelAndView("jsonView");
		User UserSession = SessionUtil.getSessionVO(request.getSession()).getUser();
		insaVO.setMb_id(UserSession.getMB_ID());
		insaVO.setView_auth((String)request.getAttribute("view_auth"));
		mv.addObject("results", getInsaService().getRecruitList(insaVO));
		return mv;
	}
	/*********************************************************** HSH인사 상세정보 리쿠르팅 ****************************************************/
}
