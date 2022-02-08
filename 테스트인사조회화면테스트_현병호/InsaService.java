package kr.co.gnx.insa;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import kr.co.gnx.base.BaseService;
import kr.co.gnx.comm.excel.excelHandler2;
import kr.co.gnx.comm.sms.SmsVO;
import kr.co.gnx.comm.util.CommUtil;
import kr.co.gnx.comm.utill.crypto.CryptoUtil;
import kr.co.gnx.config.Constants.UPLOADS;
import kr.co.gnx.insa.appoint.AppointVO;
import kr.co.gnx.insa.org.OrgVO;
import kr.co.gnx.support.smsManage.SmsManageVO;
import kr.co.gnx.system.file.FileVO;
import kr.co.gnx.system.login.LoginVO;
import kr.co.gnx.system.member.MemberVO;
import kr.co.gnx.system.role.RoleVO;
import kr.co.gnx.system.userrole.UserRoleVO;

@Service(value = "InsaService")
public class InsaService extends BaseService{
	private static final Logger logger = LoggerFactory.getLogger(InsaService.class);
	
	
	
	/**
	 * Hyun ByeongHO
	 * @param insaVO
	 * @return
	 */
	public List<Map<String, String>> getTestInsaList(InsaVO insaVO){
		List<Map<String, String>> resultList=new ArrayList<Map<String, String>>();
			resultList=getInsaDAO().selectTestInsaList(insaVO);
		return resultList;
	}//getTestInsaList
	
	
	/**
	 * @Description  : 조직 엑셀다운로드
	 * @author       : lakhyun.kim
	 * @since        : 2018. 05. 25
	 * @return       : excelHandler2
	 */
	public excelHandler2 getInsaTestListExcel(InsaVO insaVO){
		excelHandler2 eh=null;
		
		ArrayList<String> titleList = new ArrayList<String>();
		ArrayList<String> fieldsList = new ArrayList<String>();
		
		titleList = new ArrayList<String>(Arrays.asList(
				"사원번호","사원명","조직명","재직구분","직급","직책","입사일자"
				,"위촉일자","퇴사/해촉일자","은행","계좌번호"
				));
		
		fieldsList = new ArrayList<String>(Arrays.asList(
				"emp_cd","emp_nm","snmpath","empsta","jikgub_nm","jikchk_nm","ent_ymd"
				,"gent_ymd","end_ymd","bank","bk_id"
				));
		
//		String str1 = "조직경로";
//		String str2 = "snmpath";
//		String queryStr = "";
//		
//		int max = Integer.parseInt(getOrgDAO().selectOrgMaxLv(insaVO.getMb_id()));
//		
//		//최대 조직 경로 갯수만큼 컬럼을 동적으로 생성
//		for(int i=0; i<max ; i++) {
//			String num = (i+1)+"";
//			titleList.add(str1+num);
//			fieldsList.add(str2+num);
//			if("1".equals(num)) {
//				queryStr += ", fn_get_pathroot(SUBSTRING(tb7.snmpath,4), ' > ', "+num+") AS snmpath"+num;
//			}else {
//				queryStr += ", fn_get_pathroot(SUBSTRING(tb7.snmpath,2), ' > ', "+num+") AS snmpath"+num;
//			}
//		}
//		insaVO.setQueryStr(queryStr);
		
		eh = new excelHandler2(insaVO.getExcelpath() ,titleList, fieldsList);
		getInsaDAO().selectInsaTestListExcel(insaVO, eh);

		//컬럼 사이즈 설정
		if(eh.getRowindex() != 0) {
			for(int i=0; i<titleList.size(); i++) {
				eh.getDataSheet().setColumnWidth(i, (eh.getDataSheet().getColumnWidth(i)) + 2048); //(int)1 : 약 0.03픽셀
			}
		}
		return eh;
	}
	
	
	/**
	 * @Description  : 등록관련 사원찾기팝업 사원조회
	 * @author       : lakhyun.kim
	 * @since        : 2018. 05. 25
	 * @return       : List<InsaVO>
	 */
	public List<InsaVO> getEmpSchList(InsaVO insaVO){
		List<InsaVO> resultList = new ArrayList<InsaVO>();
		resultList = getInsaDAO().selectEmpSchList(insaVO);
		return resultList;
	}
	
	/**
	 * @Description  : 권한사원변경 팝업 사원조회
	 * @author       : lakhyun.kim
	 * @since        : 2019. 03. 27
	 * @return       : List<InsaVO>
	 */
	public List<InsaVO> getEmpSrchRoleList(InsaVO insaVO){
		List<InsaVO> resultList = new ArrayList<InsaVO>();
		resultList = getInsaDAO().selectEmpSrchRoleList(insaVO);
		return resultList;
	}
	
	/**
	 * @brief 인사목록조회
	 * @param insaVO
	 * @return List<Map<String, String>>
	 */
	public List<Map<String, String>> getInsaList(InsaVO insaVO){
		List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();

		resultList = getInsaDAO().selectInsaList(insaVO);
		return resultList;
	}
	
	/**
	 * @Description  : 인사 정보 엑셀다운로드
	 * @author       : lakhyun.kim
	 * @since        : 2018. 06. 04
	 * @return       : excelHandler2
	 */
	public excelHandler2 getInsaListExcel(InsaVO insaVO){
		String mb_id = insaVO.getMb_id();
		String role_id = insaVO.getRole_id();
		ArrayList<String> titleList = null;
		ArrayList<String> fieldsList = null;

		excelHandler2 eh = null;
		if("TBFS".equals(mb_id)) {
			if("ROLE_0".equals(role_id) || "ROLE_1".equals(role_id) ) {
				titleList = new ArrayList<String>(Arrays.asList(
						"사번","사원이름","사원영문이름","소속코드","소속명","주민번호","직급","직책","권한",
						"재직구분","자격구분","입사구분","급여구분","입사일","위촉일","퇴사/해촉","말소일",
						"유치방식","유치자번호","유치자","휴대전화","전화번호","이메일","우편번호","주소","상세주소",
						"생보등록일","손보등록일","변액등록일","변액등록번호","협회등록번호","경력여부","계좌번호","개설은행","개설은행코드","예금주","예금주와의관계", "메모"
				));
				
				fieldsList = new ArrayList<String>(Arrays.asList(
						"emp_cd","emp_nm","emp_nme","scd","snm","perno","jikgub_nm","jikchk_nm","role_nm",
						"empsta","insco_emp_cd_gbn_nm","join_gbn","pay_gbn","ent_ymd","gent_ymd","end_ymd","out_ymd",
						"rcrt_typenm","re_emp_cd","re_emp_nm","hpno","telno","email","zipcd","addr1","addr2",
						"lreg_ymd","nreg_ymd","vreg_ymd","vreg_no","areg_no","career_gbn_nm","bk_id","bank_nm","bank","owner","owner_relationnm", "memo"
				));
			} else if ("ROLE_2".equals(role_id) || "ROLE_3".equals(role_id)){
				titleList = new ArrayList<String>(Arrays.asList(
						"사번","사원이름","사원영문이름","소속코드","소속명","주민번호","직급","직책","권한",
						"재직구분","자격구분","입사구분","급여구분","입사일","위촉일","퇴사/해촉","말소일",
						"유치방식","유치자번호","유치자",
						"생보등록일","손보등록일","변액등록일","변액등록번호","협회등록번호","경력여부"
				));
				
				fieldsList = new ArrayList<String>(Arrays.asList(
						"emp_cd","emp_nm","emp_nme","scd","snm","perno_masking","jikgub_nm","jikchk_nm","role_nm",
						"empsta","insco_emp_cd_gbn_nm","join_gbn","pay_gbn","ent_ymd","gent_ymd","end_ymd","out_ymd",
						"rcrt_typenm","re_emp_cd","re_emp_nm",
						"lreg_ymd","nreg_ymd","vreg_ymd","vreg_no","areg_no","career_gbn_nm"
				));	
			} else {//관리자,영업자 제외 사원주민번호 마스킹처리 perno -> perno_masking
				titleList = new ArrayList<String>(Arrays.asList(
						"사번","사원이름","사원영문이름","소속코드","소속명","주민번호","직급","직책","권한",
						"재직구분","자격구분","입사구분","입사일","위촉일","퇴사/해촉","말소일",
						"유치자번호","유치자",
						"생보등록일","손보등록일","변액등록일","변액등록번호","협회등록번호"
						));
				
				fieldsList = new ArrayList<String>(Arrays.asList(
						"emp_cd","emp_nm","emp_nme","scd","snm","perno_masking","jikgub_nm","jikchk_nm","role_nm",
						"empsta","insco_emp_cd_gbn_nm","join_gbn","ent_ymd","gent_ymd","end_ymd","out_ymd",
						"re_emp_cd","re_emp_nm",
						"lreg_ymd","nreg_ymd","vreg_ymd","vreg_no","areg_no"
						));
			}
		} else if("FCN".equals(mb_id)) {
			//FCNEST 별도 다운로드 처리 - 20.07.30 KIMDONGUK
			titleList = new ArrayList<String>(Arrays.asList(
					"사번","사원이름","세무전산번호","사원영문이름","소속코드","소속명","주민번호","주소","직급","직책","권한",
					"재직구분","자격구분","입사구분","급여구분","입사일","위촉일","퇴사/해촉","말소일",
					"유치방식","유치자번호","유치자","휴대전화","전화번호","이메일","우편번호",
					"생보등록일","손보등록일","변액등록일","변액등록번호","협회등록번호","계좌번호","개설은행","개설은행코드","예금주","예금주와의관계"
			));
			
			fieldsList = new ArrayList<String>(Arrays.asList(
					"emp_cd","emp_nm","tax_elecno","emp_nme","scd","snm","perno","addr","jikgub_nm","jikchk_nm","role_nm",
					"empsta","insco_emp_cd_gbn_nm","join_gbn","pay_gbn","ent_ymd","gent_ymd","end_ymd","out_ymd",
					"rcrt_typenm","re_emp_cd","re_emp_nm","hpno","telno","email","zipcd",
					"lreg_ymd","nreg_ymd","vreg_ymd","vreg_no","areg_no","bk_id","bank_nm","bank","owner","owner_relationnm"
			));
			
		} else if("NIKE".equals(mb_id)) {
			//NIKE 별도 다운로드 처리(생/손보 등록사 추가) - 20.11.27 KIMBOSUNG
			titleList = new ArrayList<String>(Arrays.asList(
					"사번","사원이름","사원영문이름","소속코드","소속명","주민번호","직급","직책","권한",
					"재직구분","자격구분","입사구분","급여구분","입사일","위촉일","퇴사/해촉","말소일",
					"유치방식","유치자번호","유치자","휴대전화","전화번호","이메일","우편번호","주소","상세주소",
					"생보등록사","생보등록일","손보등록사","손보등록일","변액등록일","변액등록번호","협회등록번호","계좌번호","개설은행","개설은행코드","예금주","예금주와의관계"
			));
			
			fieldsList = new ArrayList<String>(Arrays.asList(
					"emp_cd","emp_nm","emp_nme","scd","snm","perno","jikgub_nm","jikchk_nm","role_nm",
					"empsta","insco_emp_cd_gbn_nm","join_gbn","pay_gbn","ent_ymd","gent_ymd","end_ymd","out_ymd",
					"rcrt_typenm","re_emp_cd","re_emp_nm","hpno","telno","email","zipcd","addr1","addr2",
					"lreg_insco_nm", "lreg_ymd","nreg_insco_nm", "nreg_ymd","vreg_ymd","vreg_no","areg_no","bk_id","bank_nm","bank","owner","owner_relationnm"
			));
		} else if("TOP".equals(mb_id)) {
			titleList = new ArrayList<String>(Arrays.asList(
					"사번","사원이름","사원영문이름","소속코드","소속명","주민번호","직급","직책","권한",
					"재직구분","자격구분","입사구분","급여구분","입사일","위촉일","재입사일","퇴사/해촉","말소일",
					"유치방식","유치자번호","유치자","휴대전화","전화번호","이메일","우편번호","주소","상세주소",
					"생보등록일","손보등록일","변액등록일","변액등록번호","협회등록번호","계좌번호","개설은행","개설은행코드","예금주","예금주와의관계"
			));
			
			fieldsList = new ArrayList<String>(Arrays.asList(
					"emp_cd","emp_nm","emp_nme","scd","snm","perno","jikgub_nm","jikchk_nm","role_nm",
					"empsta","insco_emp_cd_gbn_nm","join_gbn","pay_gbn","ent_ymd","gent_ymd","reentry_ymd","end_ymd","out_ymd",
					"rcrt_typenm","re_emp_cd","re_emp_nm","hpno","telno","email","zipcd","addr1","addr2",
					"lreg_ymd","nreg_ymd","vreg_ymd","vreg_no","areg_no","bk_id","bank_nm","bank","owner","owner_relationnm"
			));
		} else if("AFG".equals(mb_id)) {
			titleList = new ArrayList<String>(Arrays.asList(
					"재직구분","사번","사원이름","사원영문이름","주민번호","직급","직책","Region","Studio", "Branch", "Team","Office",
					"입사일","위촉일","퇴사/해촉","해촉사유","말소일","휴대전화","권한","경력구분","자격구분","입사구분","급여구분","적용위촉일", "발령일자",
					"유치방식","유치자사번","유치자","전화번호","이메일","우편번호","주소","상세주소",
					"생보등록사","생보등록일","생보말소일","손보등록사","손보등록일","손보말소일","변액등록일","변액등록번호","협회등록번호","협회등록여부","정책지원수수료","계좌번호","개설은행","개설은행코드","예금주","예금주와의관계", "메모"
					, "고용보험 취득일", "고용보험 상실일"
			));
			
			fieldsList = new ArrayList<String>(Arrays.asList(
					"empsta_nm","emp_cd","emp_nm","emp_nme","perno","jikgub_nm","jikchk_nm","snmpath3","snmpath4", "snmpath5", "snmpath6", "office_nm",
					"ent_ymd","gent_ymd","end_ymd","out_reason_nm","out_ymd","hpno","role_nm","career_gbn_nm","insco_emp_cd_gbn_nm","join_gbn","pay_gbn","apgent_ymd", "appoint_ymd",
					"rcrt_typenm","re_emp_cd","re_emp_nm","telno","email","zipcd","addr1","addr2",
					"lreg_insco_cd","lreg_ymd","lreg_out_ymd","nreg_insco_cd","nreg_ymd","nreg_out_ymd","vreg_ymd","vreg_no","areg_no","areg_yn_nm","settlement_gbn_nm","bk_id","bank_nm","bank","owner","owner_relationnm", "memo"
					,"gain_ymd","lost_ymd"
			));
		} else if("EXL".equals(mb_id)) {
			titleList = new ArrayList<String>(Arrays.asList(
					"사번","사원이름","사원영문이름","소속코드","소속명","주민번호","직급","직책","권한",
					"재직구분","자격구분","입사구분","급여구분","입사일", "재입사일", "위촉일","퇴사/해촉","말소일", "고용보험 취득일", "고용보험 상실일",
					"유치방식","유치자번호","유치자","휴대전화","전화번호","이메일","우편번호","주소","상세주소",
					"생보등록일","손보등록일","변액등록일","변액등록번호","협회등록번호","계좌번호","개설은행","개설은행코드","예금주","예금주와의관계"
			));
			
			fieldsList = new ArrayList<String>(Arrays.asList(
					"emp_cd","emp_nm","emp_nme","scd","snm","perno","jikgub_nm","jikchk_nm","role_nm",
					"empsta","insco_emp_cd_gbn_nm","join_gbn","pay_gbn","ent_ymd", "reentry_ymd","gent_ymd","end_ymd","out_ymd","gain_ymd","lost_ymd",
					"rcrt_typenm","re_emp_cd","re_emp_nm","hpno","telno","email","zipcd","addr1","addr2",
					"lreg_ymd","nreg_ymd","vreg_ymd","vreg_no","areg_no","bk_id","bank_nm","bank","owner","owner_relationnm"
			));			
		} else if("ITX".equals(mb_id)) {
			titleList = new ArrayList<String>(Arrays.asList(
					"사번","사원이름","사원영문이름","소속코드","소속명","주민번호","직급","직책","권한",
					"재직구분","자격구분","입사구분","급여구분","선지급/분급구분","적립여부","입사일","위촉일","퇴사/해촉","말소일",
					"유치방식","유치자번호","유치자","휴대전화","전화번호","이메일","우편번호","주소","상세주소",
					"생보등록일","손보등록일","변액등록일","변액등록번호","협회등록번호","계좌번호","개설은행","개설은행코드","예금주","예금주와의관계"
			));
			
			fieldsList = new ArrayList<String>(Arrays.asList(
					"emp_cd","emp_nm","emp_nme","scd","snm","perno","jikgub_nm","jikchk_nm","role_nm",
					"empsta","insco_emp_cd_gbn_nm","join_gbn","pay_gbn","bun_gbn","earn_gbn_nm","ent_ymd","gent_ymd","end_ymd","out_ymd",
					"rcrt_typenm","re_emp_cd","re_emp_nm","hpno","telno","email","zipcd","addr1","addr2",
					"lreg_ymd","nreg_ymd","vreg_ymd","vreg_no","areg_no","bk_id","bank_nm","bank","owner","owner_relationnm"
			));
		} else if("GAMC".equals(mb_id)) {
			titleList = new ArrayList<String>(Arrays.asList(
					"사번","사원이름","사원영문이름","소속코드","소속명", "유치자", "주민번호","직급","직책","권한",
					"재직구분","자격구분","입사구분","급여구분","입사일","위촉일","퇴사/해촉","말소일",
					"유치방식","유치자번호","유치자","휴대전화","전화번호","이메일","우편번호","주소","상세주소",
					"생보등록일","손보등록일","변액등록일","변액등록번호","협회등록번호","계좌번호","개설은행","개설은행코드","예금주","예금주와의관계"
			));
			
			fieldsList = new ArrayList<String>(Arrays.asList(
					"emp_cd","emp_nm","emp_nme","scd","snm", "re_emp", "perno","jikgub_nm","jikchk_nm","role_nm",
					"empsta","insco_emp_cd_gbn_nm","join_gbn","pay_gbn","ent_ymd","gent_ymd","end_ymd","out_ymd",
					"rcrt_typenm","re_emp_cd","re_emp_nm","hpno","telno","email","zipcd","addr1","addr2",
					"lreg_ymd","nreg_ymd","vreg_ymd","vreg_no","areg_no","bk_id","bank_nm","bank","owner","owner_relationnm"
			));					
		} else {
			titleList = new ArrayList<String>(Arrays.asList(
					"사번","사원이름","사원영문이름","소속코드","소속명", "주민번호","직급","직책","권한",
					"재직구분","자격구분","입사구분","급여구분","입사일","위촉일","퇴사/해촉","말소일",
					"유치방식","유치자번호","유치자","휴대전화","전화번호","이메일","우편번호","주소","상세주소",
					"생보등록일","손보등록일","변액등록일","변액등록번호","협회등록번호","계좌번호","개설은행","개설은행코드","예금주","예금주와의관계"
			));
			
			fieldsList = new ArrayList<String>(Arrays.asList(
					"emp_cd","emp_nm","emp_nme","scd","snm","perno","jikgub_nm","jikchk_nm","role_nm",
					"empsta","insco_emp_cd_gbn_nm","join_gbn","pay_gbn","ent_ymd","gent_ymd","end_ymd","out_ymd",
					"rcrt_typenm","re_emp_cd","re_emp_nm","hpno","telno","email","zipcd","addr1","addr2",
					"lreg_ymd","nreg_ymd","vreg_ymd","vreg_no","areg_no","bk_id","bank_nm","bank","owner","owner_relationnm"
			));
		}
		
		String str1 = "조직경로";
		String str2 = "snmpath";
		String queryStr = "";
		
		//뉴니케 소속명 대신 소속경로3, 소속경로 4가 나오도록하려고
		if("NIKE".equals(mb_id)) {
			titleList.remove(4);
			fieldsList.remove(4);
			
			titleList.add(4, "소속경로3");
			fieldsList.add(4, "snmpath3");
			
			titleList.add(5, "소속경로4");
			fieldsList.add(5, "snmpath4");
		}
		
		int max = Integer.parseInt(getOrgDAO().selectOrgMaxLv(insaVO.getMb_id()));
		
		//최대 조직 경로 갯수만큼 컬럼을 동적으로 생성
		if(!"AFG".equals(mb_id)) {
			for(int i=0; i<max ; i++) {
				String num = (i+1)+"";
				titleList.add(str1+num);
				fieldsList.add(str2+num);
				if("1".equals(num)) {
					queryStr += ", fn_get_pathroot(SUBSTRING(tb8.snmpath,4), ' > ', "+num+") AS snmpath"+num;
				}else {
					queryStr += ", fn_get_pathroot(SUBSTRING(tb8.snmpath,2), ' > ', "+num+") AS snmpath"+num;
				}
			}
		}
		
		insaVO.setQueryStr(queryStr);
		
		eh = new excelHandler2(insaVO.getExcelpath() ,titleList, fieldsList);
		
		getInsaDAO().selectInsaListExcel(insaVO,eh);

		//컬럼 사이즈 설정
		if(eh.getRowindex() != 0) {
			for(int i=0; i<titleList.size(); i++) {
				eh.getDataSheet().setColumnWidth(i, (eh.getDataSheet().getColumnWidth(i)) + 2048); //(int)1 : 약 0.03픽셀
			}
		}

		return eh;
	}
	
	/**
	 * @Description  : 사원정보 조회
	 * @author       : lakhyun.kim
	 * @since        : 2018. 05. 30
	 * @return       : Map
	 */
	public Map getEmpView(InsaVO insaVO){
		Map resultMap = new HashMap();
		resultMap = getInsaDAO().selectEmpView(insaVO);
		return resultMap;
	}
	
	/**
	 * @Description  : 인사 등록
	 * @author       : lakhyun.kim
	 * @since        : 2018. 05. 30
	 * @return       : InsaVO
	 */
	public InsaVO insertInsa(MultipartHttpServletRequest request, InsaVO insaVO){
		String emp_cd = "";
		insaVO.setMb_id(getUserSession().getMB_ID());
		insaVO.setIn_emp_cd(getUserSession().getEMP_CD());
		insaVO.setUp_emp_cd(getUserSession().getEMP_CD());
		
		//insaVO.setScd(getUserSession().getSCD());
		
		// 사원코드 셋팅 [1997+0000]
		emp_cd = getInsaDAO().selectEmpcdView(insaVO);
		insaVO.setEmp_cd(emp_cd);
		
		// 사원정보 등록 > TBIN_EMPMST
		getInsaDAO().insertInsa(insaVO);

		// 사원등록시 로그인정보 등록 > TBSY_LOGIN
		LoginVO loginVO = new LoginVO();
		
		// 인사등록 시 사용자 비밀번호 주민등록 앞에서 6자리로 셋팅
		String encrypedSHA256PW = "";
		try {
			encrypedSHA256PW = CryptoUtil.encryptlogin(insaVO.getPerno().substring(0,6));
		} catch (Exception e) {
			logger.info(e.getMessage());
		}
		
		/*세션정보 Start*/
		loginVO.setMb_id(getUserSession().getMB_ID());
		loginVO.setIn_emp_cd(getUserSession().getEMP_CD());
		loginVO.setUp_emp_cd(getUserSession().getEMP_CD());
		/*세션정보 End*/
		loginVO.setEmp_cd(insaVO.getEmp_cd());
		loginVO.setLogin_id(insaVO.getEmp_cd());
		loginVO.setUser_pwd(encrypedSHA256PW);
		loginVO.setTrns_user_pwd(encrypedSHA256PW);
		loginVO.setPwd_intzn_type("N");
		loginVO.setAcct_lock_type("N");
		loginVO.setPwd_err_nbtm("0");
		loginVO.setPwd_chg_dt(CommUtil.getCurrentDate());
		loginVO.setMemo("EMP_CD");
		
		// 2tactor인증여부 초기 셋팅
		RoleVO roleVO = new RoleVO();
		roleVO.setRole_id(insaVO.getRole_id());
		roleVO.setMb_id(getUserSession().getMB_ID());
		roleVO.setIn_emp_cd(getUserSession().getEMP_CD());
		roleVO.setUp_emp_cd(getUserSession().getEMP_CD());

		Map<String, String> RoleMap = getRoleDAO().selectRoleView(roleVO);

		if(RoleMap != null) {
			loginVO.setLogin_autr_type(RoleMap.get("login_autr_type"));
		}

		getLoginDAO().insertLogin(loginVO);
		
		// 권한처리에서 필요한 부분 > tbsy_user_role_map
		UserRoleVO userRoleVO = new UserRoleVO();
		userRoleVO.setEmp_cd(insaVO.getEmp_cd());
		userRoleVO.setRole_id(insaVO.getRole_id());
		userRoleVO.setScd(insaVO.getScd());       		//소속
		userRoleVO.setJikchk(insaVO.getJikchk());     	//직책
		userRoleVO.setJikgub(insaVO.getJikgub());     	//직급
		
		if("퇴직".equals(insaVO.getEmpsta())) {
			userRoleVO.setRole_id("ROLE_100");   	//권한
		}else {
			userRoleVO.setRole_id(insaVO.getRole_id());   	//권한
		}
		
		userRoleVO.setConcurrent_idx("1");				//주 권한
		userRoleVO.setMb_id(getUserSession().getMB_ID());
		userRoleVO.setIn_emp_cd(getUserSession().getEMP_CD());
		userRoleVO.setUp_emp_cd(getUserSession().getEMP_CD());
		
		getUserRoleDAO().insertUserRole(userRoleVO);
		
		//발령정보 등록(입사)
		AppointVO appointVO = new AppointVO();
		appointVO.setMb_id(getUserSession().getMB_ID());
		appointVO.setIn_emp_cd(getUserSession().getEMP_CD());
		appointVO.setUp_emp_cd(getUserSession().getEMP_CD());
		appointVO.setEmp_cd(insaVO.getEmp_cd());      //사원번호
		appointVO.setAppoint_ymd(insaVO.getEnt_ymd()); //발령일자
		appointVO.setAppoint_gbn("1");            		//발령 구분 입사 강제입력
		appointVO.setTo_scd(insaVO.getScd());       	//소속
		appointVO.setTo_jikchk(insaVO.getJikchk());     //직책
		appointVO.setTo_jikgub(insaVO.getJikgub());     //직급
		appointVO.setTo_role_id(userRoleVO.getRole_id());   //권한
				  
		getAppointDAO().insertAppoint(appointVO);
		
		/************ 첨부파일 저장 ************/
		// 프로필 파일 : pic_file, 첨부파일 : FILE_NM

		// 2. 파일리스트 
		ArrayList<FileVO> fileList = new ArrayList<FileVO>();
		if(insaVO.getFileList() != null){
			fileList = insaVO.getFileList();
		}

		// 멀티파트파일 리스트, 위의 파일리스트(폼) 데이터와 일치하지 않는다.
		MultipartFile mpf = request.getFile("pic_file");	// 프로필 사진
        List<MultipartFile> mpf2 = request.getFiles("file_nm");	// 첨부파일

        FileVO filevo = new FileVO();
        
		/*세션정보 Start*/
		filevo.setMb_id(getUserSession().getMB_ID());
		filevo.setIn_emp_cd(getUserSession().getEMP_CD());
		filevo.setUp_emp_cd(getUserSession().getEMP_CD());
		
		/*세션정보 End*/
		getFileService().setUserSession(getUserSession());
		
		// 프로필 사진 저장
		if(mpf != null) {
			if(mpf.getName().equals("pic_file")){
				filevo = getFileService().fileSave(mpf, UPLOADS.IMAGE);
				insaVO.setFile_name(filevo.getFile_url());
				// 프로필 파일경로 등록
		        getInsaDAO().updateFilename(insaVO);
		    }
		}
            
		// 첨부파일 저장
		for(int i = 0; i < mpf2.size(); i++) {
		    if(mpf2.get(i).getName().equals("file_nm")){
		    	FileVO file = fileList.get(i);
		    	file.setMb_id(getUserSession().getMB_ID());
		    	file.setIn_emp_cd(getUserSession().getEMP_CD());
		    	file.setUp_emp_cd(getUserSession().getEMP_CD());
		    	file.setEmp_cd(insaVO.getEmp_cd());
		    	file.setRef_seq(insaVO.getSeq());
		    	file.setAttach_gbn("1");
		    	getFileService().insertOrUpdateDocument(mpf2.get(i), UPLOADS.INSA_FILE, file);
		    }
		}
		
		try {
			MemberVO memberVO = new MemberVO();
			memberVO.setMb_id(getUserSession().getMB_ID());
			Map<String, String> memberMap = getMemberDAO().selectMemberView(memberVO);
			
			String companyTelno = "";
			
			if(CommUtil.isNotEmpty(memberMap)) {
				companyTelno = memberMap.get("telno");
				
				//회사 대표번호 있고, 인사 등록 핸드폰번호 있고, 재직인원이고 AT에셋이 아니면 문자전송
				if(CommUtil.isNotEmpty(companyTelno) && CommUtil.isNotEmpty(insaVO.getHpno())
						&& CommUtil.isEquals("재직", insaVO.getEmpsta()) && CommUtil.isNotEquals("AT", insaVO.getMb_id())) {
					
					// SMS 전송
					SmsVO smsVO = new SmsVO();
					smsVO.setCust_hp(insaVO.getHpno().replaceAll("-", ""));
					smsVO.setMsg_type("4");
					smsVO.setMms_subject("["+ getUserSession().getMB_NM() + "]ERP 시스템에 귀하의 인사정보가 등록되었습니다.");
					smsVO.setIn_emp_cd(getUserSession().getMB_ID()+":"+getUserSession().getEMP_CD());
					smsVO.setSms_contents("["+ getUserSession().getMB_NM() + "]ERP 시스템에 귀하의 인사정보가 등록되었습니다.\r\n"
							+ "ERP 주소 : " + memberMap.get("domain_url") + "\r\n"
							+ "ID : " + insaVO.getEmp_cd() + "\r\n"
							+ "PWD : 초기비밀번호는 주민번호 앞자리입니다. ERP 시스템에 로그인하신 후 비밀번호 변경하시기 바랍니다.");
					smsVO.setMb_id(getUserSession().getMB_ID());
					smsVO.setIn_emp_cd(getUserSession().getEMP_CD());
					smsVO.setSend_hp(companyTelno);
					smsVO.setSms_action_type("INSA");
					
					if(smsVO.getSms_contents()!=null && smsVO.getSms_contents().getBytes().length<80) {
						smsVO.setMsg_type("4");
					}else{
						smsVO.setMsg_type("6");
					}
					getSmsDAO().insertOnlyTextSmsData(smsVO);
				}
			}
		}catch(Exception e) {
			logger.error("문자전송에 실패했습니다.");
			logger.error(e.getMessage());
		}
		
		return insaVO;
	}
	
	/**
	 * @Description  : 인사 수정
	 * @author       : lakhyun.kim
	 * @since        : 2018. 06. 04
	 * @return       : InsaVO
	 */
	public InsaVO updateInsa(MultipartHttpServletRequest request, InsaVO insaVO){
			/*세션정보 Start*/
			insaVO.setMb_id(getUserSession().getMB_ID());
			insaVO.setIn_emp_cd(getUserSession().getEMP_CD());
			insaVO.setUp_emp_cd(getUserSession().getEMP_CD());
			/*세션정보 End*/
			// 사원정보 수정 > TBIN_EMPMST
			getInsaDAO().updateInsa(insaVO);
			
			// 권한처리에서 필요한 부분 수정 > TBSY_USER_ROLE_MAP
			UserRoleVO userRoleVO = new UserRoleVO();
			userRoleVO.setEmp_cd(insaVO.getEmp_cd());
			userRoleVO.setRole_id(insaVO.getRole_id());
			userRoleVO.setScd(insaVO.getScd());       		//소속
			userRoleVO.setJikchk(insaVO.getJikchk());     	//직책
			userRoleVO.setJikgub(insaVO.getJikgub());     	//직급
			
			if("퇴직".equals(insaVO.getEmpsta())) {
				userRoleVO.setRole_id("ROLE_100");   	//권한
			}else if ("해촉".equals(insaVO.getEmpsta())){
				userRoleVO.setRole_id("ROLE_100");   	//권한
			}else if ("미위촉퇴사".equals(insaVO.getEmpsta())){
				userRoleVO.setRole_id("ROLE_100");   	//권한
			}else {
				userRoleVO.setRole_id(insaVO.getRole_id());   	//권한
			}
			
			userRoleVO.setConcurrent_idx("1");				//주 권한
			userRoleVO.setMb_id(getUserSession().getMB_ID());
			userRoleVO.setIn_emp_cd(getUserSession().getEMP_CD());
			userRoleVO.setUp_emp_cd(getUserSession().getEMP_CD());
			
			getUserRoleDAO().updateUserRole(userRoleVO);
			
			if("Y".equals(insaVO.getAppoint_flag())) {//발령관련 사항이 수정되었을 때
				// 발령정보 등록(인사정보 직접수정)
				AppointVO appointVO = new AppointVO();
				appointVO.setMb_id(getUserSession().getMB_ID());
				appointVO.setIn_emp_cd(getUserSession().getEMP_CD());
				appointVO.setUp_emp_cd(getUserSession().getEMP_CD());
				appointVO.setEmp_cd(insaVO.getEmp_cd());		//사원번호
				appointVO.setAppoint_ymd(CommUtil.getCurrentDateYYYYMMDD());	//발령일자
				appointVO.setAppoint_gbn("8");            		//발령 구분 인사정보 직접수정 강제입력
				appointVO.setTo_scd(insaVO.getScd());       	//소속
				appointVO.setTo_jikchk(insaVO.getJikchk());     //직책
				appointVO.setTo_jikgub(insaVO.getJikgub());     //직급
				appointVO.setTo_role_id(userRoleVO.getRole_id());   //권한
	
				getAppointDAO().insertAppoint(appointVO);
			}
			
			getFileService().setUserSession(getUserSession());
			/************ 첨부파일 수정 ************/
			// 프로필 파일 : pic_file, 첨부파일 : file_nm

			FileVO filevo = new FileVO();

			// 멀티파트파일 리스트, 위의 파일리스트(폼) 데이터와 일치하지 않는다.
			MultipartFile mpf = request.getFile("pic_file");	// 프로필 사진
			// 프로필 사진 저장
			if(null != mpf) {
				if("pic_file".equals(mpf.getName())){
					filevo = getFileService().fileSave(mpf, UPLOADS.IMAGE);
					insaVO.setFile_name(filevo.getFile_url());
					// 프로필 파일경로 등록
					getInsaDAO().updateFilename(insaVO);
				}
			}
			
            List<MultipartFile> mpf2 = request.getFiles("file_nm");	// 첨부파일
            


			// 2. 파일리스트 
			ArrayList<FileVO> fileList = new ArrayList<FileVO>();
			if(insaVO.getFileList() != null){
				fileList = insaVO.getFileList();
			}

			// 추가 첨부파일 저장 리스트
			ArrayList<FileVO> newFileList = new ArrayList<FileVO>();
			for (int i = 0; i< fileList.size(); i++) {
				if(null == fileList.get(i).getFile_no() || "".equals(fileList.get(i).getFile_no())){
					// 추가되는 첨부파일 저장 (추후에 추가하기위해 구분)
					newFileList.add(fileList.get(i));
				}
			}
			
			/*세션정보 Start*/
			filevo.setMb_id(getUserSession().getMB_ID());
			filevo.setIn_emp_cd(getUserSession().getEMP_CD());
			filevo.setUp_emp_cd(getUserSession().getEMP_CD());
			filevo.setAttach_gbn("1");
			/*세션정보 End*/
            /************ 첨부파일 수정 ************/
			// 2. 첨부파일 리스트 처리
            if(mpf2 != null) {
				filevo.setRef_seq(insaVO.getSeq());
				List<FileVO> fileListFromDb = getFileDAO().selectFileListByRefSeq(filevo);		//첨부구분(attach_gbn), ref_seq로만 조회
				
				for(int l = 0; l < fileListFromDb.size(); l++){	// DB리스트
					boolean isSameFileNo = false;
					
					for(int i = 0; i < fileList.size(); i++){	// 리스트
						
						// 파일번호가 같을때는 삭제 안함 (기존 파일 삭제 안함)
						if(fileListFromDb.get(l).getFile_no().equals(fileList.get(i).getFile_no())){
							isSameFileNo = true;
						}
					}

					// 파일번호가 없으면 삭제 (기존 파일 삭제)
					if(isSameFileNo == false) {
						FileVO fileromDb = fileListFromDb.get(l);
						fileromDb.setMb_id(getUserSession().getMB_ID());
						fileromDb.setIn_emp_cd(getUserSession().getEMP_CD());
						fileromDb.setUp_emp_cd(getUserSession().getEMP_CD());
						fileromDb.setAttach_gbn("1");
						getFileDAO().deleteFileInfo(fileromDb);
					}
				}
				
				// 파일번호가 없으면 입력(상단에서 정의한 새로운 파일 저장)
				for( int m = 0; m < newFileList.size(); m++){
					if(newFileList.get(m).getFile_no() == null || newFileList.get(m).getFile_no().equals("")){
						FileVO file = newFileList.get(m);
						file.setEmp_cd(insaVO.getEmp_cd());
						file.setRef_seq(insaVO.getSeq());
						file.setMb_id(getUserSession().getMB_ID());
						file.setIn_emp_cd(getUserSession().getEMP_CD());
						file.setUp_emp_cd(getUserSession().getEMP_CD());
						file.setAttach_gbn("1");
						getFileService().insertOrUpdateDocument(mpf2.get(m), UPLOADS.INSA_FILE, file);
					}
				}
            }
		return insaVO;
	}
	
	/**
	 * @Description  : 인사 삭제
	 * @author       : lakhyun.kim
	 * @since        : 2018. 06. 04
	 * @return       : InsaVO
	 */
	public int deleteinsa(InsaVO insaVO){
		int resultInt = 0;
		try {
			/*세션정보 Start*/
			insaVO.setMb_id(getUserSession().getMB_ID());
			insaVO.setIn_emp_cd(getUserSession().getEMP_CD());
			insaVO.setUp_emp_cd(getUserSession().getEMP_CD());
			/*세션정보 End*/
			
			// 사원삭제 시 로그인정보 삭제 > TBSY_LOGIN
			LoginVO loginVO = new LoginVO();
			
			/*세션정보 Start*/
			loginVO.setMb_id(getUserSession().getMB_ID());
			loginVO.setIn_emp_cd(getUserSession().getEMP_CD());
			loginVO.setUp_emp_cd(getUserSession().getEMP_CD());
			/*세션정보 End*/
			
			loginVO.setEmp_cd(insaVO.getEmp_cd());
			getLoginDAO().deleteLogin(loginVO);

			// 권한처리에서 필요한 부분 삭제 > TBSY_USER_ROLE_MAP
			UserRoleVO userRoleVO = new UserRoleVO();
			userRoleVO.setEmp_cd(insaVO.getEmp_cd());
			
			/*세션정보 Start*/
			userRoleVO.setMb_id(getUserSession().getMB_ID());
			userRoleVO.setIn_emp_cd(getUserSession().getEMP_CD());
			userRoleVO.setUp_emp_cd(getUserSession().getEMP_CD());
			/*세션정보 End*/
			
			getUserRoleDAO().deleteUserAllRole(userRoleVO);
			
			// 사원정보 삭제
			resultInt = getInsaDAO().deleteInsa(insaVO);
			
			//파일삭제
			FileVO file = new FileVO();
			file.setMb_id(getUserSession().getMB_ID());
			file.setAttach_gbn("1");
			file.setRef_seq(insaVO.getSeq());
			getFileDAO().deleteFileInfoByRefIdx(file);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		
		return resultInt;
	}
	
	/*********************************************************** 인사마감 ****************************************************/	
	/**
	 * @brief 인사마감목록조회
	 * @param insaVO
	 * @return List<Map<String, String>>
	 */
	public List<Map<String, String>> getMonthInsaList(InsaVO insaVO){
		List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();

		resultList = getInsaDAO().selectMonthInsaList(insaVO);
		return resultList;
	}
	
	/**
	 * @Description  : 사원정보 조회
	 * @author       : yumi.jeon
	 * @since        : 2019. 02. 27
	 * @return       : Map
	 */
	public Map getMonthEmpView(InsaVO insaVO){
		Map resultMap = new HashMap();
		resultMap = getInsaDAO().selectMonthEmpView(insaVO);
		return resultMap;
	}
	
	/**
	 * @Description  : 인사 마감 수정
	 * @author       : yumi.jeon
	 * @since        : 2019. 02. 27
	 * @return       : InsaVO
	 */
	public InsaVO updateMonthInsa(MultipartHttpServletRequest request, InsaVO insaVO){
		/*세션정보 Start*/
		insaVO.setMb_id(getUserSession().getMB_ID());
		insaVO.setIn_emp_cd(getUserSession().getEMP_CD());
		insaVO.setUp_emp_cd(getUserSession().getEMP_CD());
		/*세션정보 End*/
		// 사원정보 수정 > TBIN_EMPMST
		getInsaDAO().updateMonthInsa(insaVO);
		return insaVO;
	}
	
	/**
	 * @Description  : 인사마감 정보 엑셀다운로드
	 * @author       : yumi.jeon
	 * @since        : 2019. 02. 27
	 * @return       : excelHandler2
	 */
	public excelHandler2 getMonthInsaListExcel(InsaVO insaVO){
		String mb_id = insaVO.getMb_id();
		excelHandler2 eh = null;
		ArrayList<String> titleList = null;
		ArrayList<String> fieldsList = null;

		if("AFG".equals(mb_id)) {
			titleList = new ArrayList<String>(Arrays.asList(
					"재직구분", "사번", "사원이름", "사원영문이름", "주민번호", "직급", "직책", "Region", "Studio", "Branch",
					"Team", "입사일", "위촉일", "퇴사/해촉","해촉사유", "말소일", "휴대전화", "권한", "경력구분", "자격구분", "입사구분", "급여구분", "적용위촉일",
					"유치방식", "유치자사번", "유치자", "전화번호", "이메일", "우편번호", "주소", "상세주소", "생보등록일", "손보등록일", "변액등록일",
					"변액등록번호", "협회등록번호", "협회등록여부", "정책지원수수료", "계좌번호", "개설은행", "개설은행코드", "예금주", "예금주와의관계"));

			fieldsList = new ArrayList<String>(Arrays.asList(
					"empsta_nm", "emp_cd", "emp_nm", "emp_nme", "perno",
					"jikgub_nm", "jikchk_nm", "snmpath3", "snmpath4", "snmpath5", "snmpath6", "ent_ymd", "gent_ymd",
					"end_ymd","out_reason_nm", "out_ymd", "hpno", "role_nm", "career_gbn_nm", "insco_emp_cd_gbn_nm", "join_gbn",
					"pay_gbn", "apgent_ymd", "rcrt_typenm", "re_emp_cd", "re_emp_nm", "telno", "email", "zipcd",
					"addr1", "addr2", "lreg_ymd", "nreg_ymd", "vreg_ymd", "vreg_no", "areg_no", "areg_yn_nm",
					"settlement_gbn_nm", "bk_id", "bank_nm", "bank", "owner", "owner_relationnm"));
		}else if("ITX".equals(mb_id)) {
			titleList = new ArrayList<String>(Arrays.asList(
					"마감년월", "사번", "사원이름", "사원영문이름", "소속코드", "소속명", "주민번호", "직급",
					"직책", "권한", "재직구분", "자격구분", "입사구분", "선지급/분급 구분","입사일", "위촉일", "퇴사/해촉", "말소일", "유치방식", "유치자번호", "유치자", "휴대전화",
					"전화번호", "이메일", "우편번호", "주소", "상세주소", "생보등록일", "손보등록일", "변액등록일", "변액등록번호", "협회등록번호", "계좌번호", "개설은행",
					"예금주", "예금주와의관계"));

			fieldsList = new ArrayList<String>(Arrays.asList(
					"com_ym", "emp_cd", "emp_nm", "emp_nme", "scd", "snm",
					"perno", "jikgub_nm", "jikchk_nm", "role_nm", "empsta_nm", "insco_emp_cd_gbn_nm", "join_gbn","bun_gbn",
					"ent_ymd", "gent_ymd", "end_ymd", "out_ymd", "rcrt_typenm", "re_emp_cd", "re_emp_nm", "hpno",
					"telno", "email", "zipcd", "addr1", "addr2", "lreg_ymd", "nreg_ymd", "vreg_ymd", "vreg_no",
					"areg_no", "bk_id", "bank", "owner", "owner_relationnm"));
		} 
		else {
			titleList = new ArrayList<String>(Arrays.asList(
					"마감년월", "사번", "사원이름", "사원영문이름", "소속코드", "소속명", "주민번호", "직급",
					"직책", "권한", "재직구분", "자격구분", "입사구분", "입사일", "위촉일", "퇴사/해촉", "말소일", "유치방식", "유치자번호", "유치자", "휴대전화",
					"전화번호", "이메일", "우편번호", "주소", "상세주소", "생보등록일", "손보등록일", "변액등록일", "변액등록번호", "협회등록번호", "계좌번호", "개설은행",
					"예금주", "예금주와의관계"));

			fieldsList = new ArrayList<String>(Arrays.asList(
					"com_ym", "emp_cd", "emp_nm", "emp_nme", "scd", "snm",
					"perno", "jikgub_nm", "jikchk_nm", "role_nm", "empsta_nm", "insco_emp_cd_gbn_nm", "join_gbn",
					"ent_ymd", "gent_ymd", "end_ymd", "out_ymd", "rcrt_typenm", "re_emp_cd", "re_emp_nm", "hpno",
					"telno", "email", "zipcd", "addr1", "addr2", "lreg_ymd", "nreg_ymd", "vreg_ymd", "vreg_no",
					"areg_no", "bk_id", "bank", "owner", "owner_relationnm"));
		};

		String str1 = "조직경로";
		String str2 = "snmpath";
		String queryStr = "";
		
		int max = Integer.parseInt(getOrgDAO().selectOrgMaxLv(insaVO.getMb_id()));
		
		//최대 조직 경로 갯수만큼 컬럼을 동적으로 생성
		if(!"AFG".equals(mb_id)) {
			for(int i=0; i<max ; i++) {
				String num = (i+1)+"";
				titleList.add(str1+num);
				fieldsList.add(str2+num);
				if("1".equals(num)) {
					queryStr += ", fn_get_pathroot(SUBSTRING(tb8.snmpath,4), ' > ', "+num+") AS snmpath"+num;
				}else {
					queryStr += ", fn_get_pathroot(SUBSTRING(tb8.snmpath,2), ' > ', "+num+") AS snmpath"+num;
				}
			}
		}
		
		insaVO.setQueryStr(queryStr);
		
		eh = new excelHandler2(insaVO.getExcelpath() ,titleList, fieldsList);
		
		getInsaDAO().selectMonthInsaListExcel(insaVO,eh);

		//컬럼 사이즈 설정
		if(eh.getRowindex() != 0) {
			for(int i=0; i<titleList.size(); i++) {
				eh.getDataSheet().setColumnWidth(i, (eh.getDataSheet().getColumnWidth(i)) + 2048); //(int)1 : 약 0.03픽셀
			}
		}

		return eh;
	}
	
	/**
	 * @Description  : 인사마감 삭제
	 * @author       : yumi.jeon
	 * @since        : 2019. 03. 06
	 * @return       : InsaVO
	 */
	public int deleteMonthInsa(InsaVO insaVO){
		int resultInt = 0;
			/*세션정보 Start*/
			insaVO.setMb_id(getUserSession().getMB_ID());
			insaVO.setIn_emp_cd(getUserSession().getEMP_CD());
			insaVO.setUp_emp_cd(getUserSession().getEMP_CD());
			/*세션정보 End*/
			
			// 마감 사원정보 삭제
			resultInt = getInsaDAO().deleteMonthInsa(insaVO);
		return resultInt;
	}
	
	/*********************************************************** 평가인사 생성 ****************************************************/	
	/**
	 * @brief 평가인사목록조회
	 * @param insaVO
	 * @return List<Map<String, String>>
	 */
	public List<InsaVO> getMonthEvalInsaList(InsaVO insaVO){
		List<InsaVO> resultList = new ArrayList<InsaVO>();

		resultList = getInsaDAO().selectMonthEvalInsaList(insaVO);
		return resultList;
	}
	
	/**
	 * @Description  : 평가인사 마감 수정
	 * @author       : wonhyeok.choi
	 * @since        : 2020. 12. 11
	 * @return       : InsaVO
	 */
	public InsaVO updateMonthEvalInsaList(MultipartHttpServletRequest request, InsaVO insaVO){
		/*세션정보 Start*/
		insaVO.setMb_id(getUserSession().getMB_ID());
		insaVO.setIn_emp_cd(getUserSession().getEMP_CD());
		insaVO.setUp_emp_cd(getUserSession().getEMP_CD());
		/*세션정보 End*/
		// 사원정보 수정 > TBIN_EMPMST
		getInsaDAO().updateMonthEvalInsaList(insaVO);
		return insaVO;
	}

	/**
	 * @Description  : 평가인사 마감 리스트 엑셀다운로드
	 * @author       : wonheyok.choi	
	 * @since        : 2020. 12. 04
	 * @return       : excelHandler2
	 */
	public excelHandler2 getMonthEvalInsaListExcel(InsaVO insaVO){
		excelHandler2 eh = null;
		ArrayList<String> titleList = new ArrayList<String>(Arrays.asList(
				"마감년월","사번","사원이름","직책","재직구분","위촉일","퇴사일","해촉일","위촉차월",
				"조직정보 조직구분","조직정보 조직","조직정보 조직장코드","조직정보 조직장이름",
				"분할조직정보 분할조직여부","분할조직정보 조직분할일","분할조직정보 조직분할수수료적용기간","분할조직정보 모조직","분할조직정보 모조직장코드","분할조직정보 모조직장이름",
				"STL 조직장사번","STL 조직장이름","SM 조직장사번","SM 조직장이름","BM 조직장사번","BM 조직장이름","HM 조직장사번","HM 조직장이름"
		));
		
		ArrayList<String> fieldsList = new ArrayList<String>(Arrays.asList(
				"com_ym","emp_cd","emp_nm","jikchk","empsta","gent_ymd","end_ymd","out_ymd","",
				"org_unit_gbnnm","snm","scd_head","scd_headnm",
				"div_yn","div_ymd","div_comm_start_ymd","div_pscdnm","div_pscd_head","div_pscd_headnm",
				"lv6_scd_head","lv6_scd_headnm","lv5_scd_head","lv5_scd_headnm","lv3_scd_head","lv3_scd_headnm","lv2_scd_head","lv2_scd_headnm"
		));

		eh = new excelHandler2(insaVO.getExcelpath() ,titleList, fieldsList);
		
		getInsaDAO().selectMonthEvalInsaListExcel(insaVO,eh);

		//컬럼 사이즈 설정
		if(eh.getRowindex() != 0) {
			for(int i=0; i<titleList.size(); i++) {
				eh.getDataSheet().setColumnWidth(i, (eh.getDataSheet().getColumnWidth(i)) + 2048); //(int)1 : 약 0.03픽셀
			}
		}

		return eh;
	}
	
	/*********************************************************** 평가인사 생성 ****************************************************/	
	
	/**
	 * @Description  : 사원 조회 DB분배 대상자 기준  조회
	 * @author       : lakhyun.kim
	 * @since        : 2019. 04. 30
	 * @return       : List<InsaVO>
	 */
	public List<InsaVO> getEmpSchDbTargetList(InsaVO insaVO) {
		/*세션정보 Start*/
		insaVO.setMb_id(getUserSession().getMB_ID());
		insaVO.setIn_emp_cd(getUserSession().getEMP_CD());
		insaVO.setUp_emp_cd(getUserSession().getEMP_CD());
		/*세션정보 End*/
		return getInsaDAO().selectEmpSchDbTargetList(insaVO);
	}
	
	/**
	 * @Description  : 인사 사원 조회 DB분배 동의 대상자 기준  조회
	 * @author       : lakhyun.kim
	 * @since        : 2019. 04. 30
	 * @return       : List<InsaVO>
	 */
	public List<InsaVO> getDbTargetSchList(InsaVO insaVO) {
		/*세션정보 Start*/
		insaVO.setMb_id(getUserSession().getMB_ID());
		insaVO.setIn_emp_cd(getUserSession().getEMP_CD());
		insaVO.setUp_emp_cd(getUserSession().getEMP_CD());
		/*세션정보 End*/
		
		return getInsaDAO().selectDbTargetSchList(insaVO);
	}

	/**
	 * @Description  : 주민번호로 사원 단건 조회
	 * @author       : KIMDONGUK
	 * @since        : 2019. 06. 05
	 * @return       : Map<String, String>
	 */
	public Map<String, String> getEmpView2(InsaVO insaVO) {
		Map<String, String> resultMap = new HashMap<String, String>();
		
		resultMap = getInsaDAO().selectEmpView2(insaVO);
		
		return resultMap;
	}

	/**
	 * @Description  : 핸드폰번호 중복 조회
	 * @author       : lakhyun.kim
	 * @since        : 2019. 09. 04
	 * @return       : Map<String, String>
	 * @param        : InsaVO
	 */
	public int getEmpView3Count(InsaVO insaVO) {
		int resultInt = 0;
		resultInt = getInsaDAO().selectEmpView3Count(insaVO);
		return resultInt;
	}
	
	/**
	 * @Description  : 핸드폰번호로 사원 단건 조회
	 * @author       : lakhyun.kim
	 * @since        : 2019. 09. 04
	 * @return       : Map<String, String>
	 * @param        : InsaVO
	 */
	public Map<String, String> getEmpView3(InsaVO insaVO) {
		Map<String, String> resultMap = new HashMap<String, String>();
		resultMap = getInsaDAO().selectEmpView3(insaVO);
		return resultMap;
	}
	
	/**
	 * @Description 아이디 확인 및 인증번호 생성 및 발송(유저검색)
	 * @Author taeyoon.kim 
	 * @Date 2019. 12. 26
	 * @param InSaVO
	 * @return String
	 * @throws Exception
	 */
	public String selectEmpCount(InsaVO paramvo) throws Exception {
		int result = 0;
		String info = "";
		try {
			//인사기본정보 세팅
			result = getInsaDAO().selectEmpCount(paramvo);	
			if(result > 0){
				info = "success";
			} else {
				info = "failure";
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw e;
		}
		return info;
	}
	
	public List<Map<String, Object>> getRecruitList(InsaVO paramvo) {
		return getInsaDAO().selectRecruitList(paramvo);
	}
	
	/**
	 * @Description 아이디 확인 및 인증번호 생성 및 발송(인증번호 생성 및 발송)
	 * @author taeyoon.kim
	 * @param InSaVO 
	 * @since 2019. 12. 26
	 * @throws Exception
	 */
	public void userchecksms(InsaVO vo) throws Exception {
		SmsManageVO smsManageVo = new SmsManageVO();
		try {
			
			//인증코드 생성 
			String user_pwd = "";
			SecureRandom random = new SecureRandom();
			random.setSeed(new Date().getTime());
			for (int j = 0; j < 6; j++) {
				user_pwd += random.nextInt(10);
			}
			
			//인증번호저장
			smsManageVo.setMb_id(vo.getMb_id().toUpperCase());
			smsManageVo.setEmp_nm(vo.getEmp_nm());
			smsManageVo.setAuthkey_num(user_pwd);
			if(vo.getSRCH_TYPE().equals("1")) {
				smsManageVo.setBigo("아이디");
			} else if(vo.getSRCH_TYPE().equals("2")) {
				smsManageVo.setBigo("비밀번호");
			}
			smsManageVo.setData_dcd("S");
			//이전 인증 제거(되어있을경우)
			getSmsManageDAO().deleteusercheck(smsManageVo);
			//인증 번호 등록
			getSmsManageDAO().insertusercheck(smsManageVo);
			//인증 기록 저장
			getSmsManageDAO().insertcheckhist(smsManageVo);
			
			String mb_id = vo.getMb_id().toUpperCase();
			
			MemberVO memVo = new MemberVO();
			memVo.setMb_id(mb_id);
			Map<String, String> mbMap = getMemberDAO().selectMemberView(memVo);
			String mb_telno = null!=mbMap.get("telno")&&!mbMap.get("telno").equals("null")?mbMap.get("telno"):"02-6927-0447";
			
			SmsVO smsVO = new SmsVO();
			smsVO.setMb_id(mb_id);
        	smsVO.setCust_hp(vo.getHpno().replaceAll("-", ""));
			smsVO.setMsg_type("4");
			smsVO.setMms_subject("인증번호 요청.");
			smsVO.setSms_contents("인증번호 요청.\r\n" + "인증번호 : "+ user_pwd);
			smsVO.setSms_action_type("인증번호 요청");
			smsVO.setSend_hp(mb_telno);
			
			if(smsVO.getSms_contents()!=null && smsVO.getSms_contents().getBytes().length<80) {
				smsVO.setMsg_type("4");
			}else{
				smsVO.setMsg_type("6");
			}
			getSmsDAO().insertOnlyTextSmsData(smsVO);
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw e;
		}
	}
	
	/**
	 * @Description 유저 아이디 및 비밀번호 가져오기
	 * @Author taeyoon.kim 
	 * @Date 2019. 12. 04
	 * @param parametervo
	 * @return InSaVO
	 * @throws Exception
	 */
	public String getUserAuth(InsaVO paramvo) throws Exception {
		String result = "";
		SmsManageVO sysvo = new SmsManageVO();
		LoginVO loginVo = new LoginVO();
		try {
			//인증완료절차
			sysvo.setMb_id(paramvo.getMb_id().toUpperCase());
			sysvo.setEmp_nm(paramvo.getEmp_nm());
			if(paramvo.getSRCH_TYPE().equals("1")){ 
				sysvo.setBigo("아이디");
			} else if(paramvo.getSRCH_TYPE().equals("2")) {
				sysvo.setBigo("비밀번호");
			}
			sysvo.setData_dcd("I");
			//인증 기록 저장
			getSmsManageDAO().insertcheckhist(sysvo);
			//저장된 인증 번호 삭제
			getSmsManageDAO().deleteusercheck(sysvo);
			
			//인사기본정보 확인 및 발급
			if(paramvo.getSRCH_TYPE().equals("1")){ 
				result = getInsaDAO().getUserAuthId(paramvo);
			} else if(paramvo.getSRCH_TYPE().equals("2")) {
				//Pw 재생성 
				String user_pwd = "";
				SecureRandom random = new SecureRandom();
				random.setSeed(new Date().getTime());
				for (int j = 0; j < 8; j++) {
					user_pwd += random.nextInt(10);
				}
				
				try {
					loginVo.setUser_pwd(CryptoUtil.encryptlogin(user_pwd));
				} catch (Exception e) {
					logger.info(e.getMessage());
				}
				loginVo.setMb_id(paramvo.getMb_id().toUpperCase());
				loginVo.setUser_id(paramvo.getEmp_cd());
				String userEmp = getLoginDAO().getEmpcd(loginVo);
				loginVo.setEmp_cd(userEmp);
				loginVo.setUp_emp_cd(userEmp);
				loginVo.setAcct_lock_type("N");
				loginVo.setPwd_err_nbtm("0");
				loginVo.setPwd_chg_dt(CommUtil.getDateFormat("yyyyMMdd"));
//				logger.info("=============== USER_PWD ==========["+loginVo.getUSER_PWD()+"]"); 
				getLoginDAO().updateLogin(loginVo);
				
				String mb_id = paramvo.getMb_id().toUpperCase();
				MemberVO memVo = new MemberVO();
				memVo.setMb_id(mb_id);
				Map<String, String> mbMap = getMemberDAO().selectMemberView(memVo);
				String mb_telno = null!=mbMap.get("telno")&&!mbMap.get("telno").equals("null")?mbMap.get("telno"):"02-6927-0447";
				
				SmsVO smsVO = new SmsVO();
				smsVO.setMb_id(mb_id);
	        	smsVO.setCust_hp(paramvo.getHpno().replaceAll("-", ""));
				smsVO.setMsg_type("4");
				smsVO.setMms_subject("비밀번호 요청");
				smsVO.setSms_contents("비밀번호 요청.\r\n" + "비밀번호 : "+ user_pwd);
				smsVO.setSend_hp(mb_telno);
				smsVO.setSms_action_type("번호요청");
				
				if(smsVO.getSms_contents()!=null && smsVO.getSms_contents().getBytes().length<80) {
					smsVO.setMsg_type("4");
				}else{
					smsVO.setMsg_type("6");
				}
				getSmsDAO().insertOnlyTextSmsData(smsVO);
			}
			
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw e;
		}
		return result;
	}
}
