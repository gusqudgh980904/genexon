package kr.co.gnx.insa;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import kr.co.gnx.base.BaseDAO;
import kr.co.gnx.comm.excel.excelHandler2;
import kr.co.gnx.comm.util.CommUtil;
import kr.co.gnx.insa.appoint.AppointVO;

@Repository(value = "InsaDAO")
public class InsaDAO extends BaseDAO{
	private static final Logger logger = LoggerFactory.getLogger(InsaDAO.class);
	
	
	/**
	 * Hyeon ByenogHo
	 * @param insaVO
	 * @return
	 */
	public List<Map<String, String>> selectTestInsaList(InsaVO insaVO){
		List<Map<String, String>> resultList=new ArrayList<Map<String, String>>();
			resultList=getSqlSession().selectList(getInsamapper()+"selectTestInsaList",insaVO);
		
		return resultList;
	}//selectTestInsaList
	
	
	/**
	 * @Description  : 인사 정보 엑셀다운로드
	 * @author       : lakhyun.kim
	 * @since        : 2018. 06. 04
	 * @return       : void
	 */
	public void selectInsaTestListExcel(InsaVO insaVO , excelHandler2 eh){		
		getSqlSession().select(getInsamapper() + "selectTestInsaList", insaVO,eh);
	}
	
	
	
	/**
	 * @Description  : 등록관련 사원찾기팝업 사원조회
	 * @author       : lakhyun.kim
	 * @since        : 2018. 05. 25
	 * @return       : List<InsaVO>
	 */
	public List<InsaVO> selectEmpSchList(InsaVO insaVO){
		List<InsaVO> reulstList = new ArrayList<InsaVO>();
		reulstList = getSqlSession().selectList(getInsamapper() + "selectEmpSchList", insaVO);
		return reulstList;
	}
	
	/**
	 * @Description  : 권한사원변경 팝업 사원조회
	 * @author       : lakhyun.kim
	 * @since        : 2019. 03. 27
	 * @return       : List<InsaVO>
	 */
	public List<InsaVO> selectEmpSrchRoleList(InsaVO insaVO){
		List<InsaVO> reulstList = new ArrayList<InsaVO>();
		if(CommUtil.isNotEmpty(insaVO.getPage())) {
			reulstList = getSqlSession().selectList(getInsamapper() + "selectTestInsaList", insaVO);
		}else {
			reulstList = getSqlSession().selectList(getInsamapper() + "selectTestInsaList", insaVO);
		}
		return reulstList;
	}
	
	/**
	 * @brief 인사목록조회 
	 * @param InsaVO
	 * @return List<InsaVO>
	 */
	public List<Map<String, String>> selectInsaList(InsaVO insaVO){
		List<Map<String, String>> reulstList = new ArrayList<Map<String, String>>();
		
		if(CommUtil.isNotEmpty(insaVO.getPage())) {
			reulstList = getSqlSession().selectList(getInsamapper() + "selectInsaListPaging", insaVO);
		}else {
			reulstList = getSqlSession().selectList(getInsamapper() + "selectInsaList", insaVO);
		}
		return reulstList;
	}
	
	/**
	 * @Description  : 인사 정보 엑셀다운로드
	 * @author       : lakhyun.kim
	 * @since        : 2018. 06. 04
	 * @return       : void
	 */
	public void selectInsaListExcel(InsaVO insaVO , excelHandler2 eh){		
		getSqlSession().select(getInsamapper() + "selectInsaList", insaVO,eh);
	}
	
	/**
	 * @Description  : 사원정보 조회
	 * @author       : lakhyun.kim
	 * @since        : 2018. 05. 23
	 * @return       : Map
	 */
	public Map selectEmpView(InsaVO insaVO){
		Map resultMap = new HashMap();
		resultMap = getSqlSession().selectOne(getInsamapper() + "selectEmpView", insaVO);
		return resultMap;
	}
	
	/**
	 * @Description  : INSERT할 사원코드 조회
	 * @author       : lakhyun.kim
	 * @since        : 2018. 06. 04
	 * @return       : Map
	 */
	public String selectEmpcdView(InsaVO insaVO){
		String emp_cd = "";
		emp_cd = getSqlSession().selectOne(getInsamapper() + "selectEmpcdView", insaVO);
		return emp_cd;
	}
	
	/**
	 * @Description  : 인사 등록
	 * @author       : lakhyun.kim
	 * @since        : 2018. 05. 30
	 * @return       : int
	 */
	public int insertInsa(InsaVO insaVO){
		int resultInt= 0;
		resultInt = getSqlSession().insert(getInsamapper() + "insertInsa", insaVO);
		return resultInt;
	}
	
	/**
	 * @Description  : 인사 등록(Map)
	 * @author       : lakhyun.kim
	 * @since        : 2018. 05. 30
	 * @return       : int
	 */
	public int insertInsaMap(Map insaMap){
		int resultInt= 0;
		resultInt = getSqlSession().insert(getInsamapper() + "insertInsaMap", insaMap);
		return resultInt;
	}
	
	/**
	 * @Description  : 밸류마크 해촉일 등록(Map)
	 * @author       : yeonjeong.an
	 * @since        : 2021. 10. 19
	 * @return       : void
	 */
	public void updateInsaMap(Map insaMap){
		getSqlSession().update(getInsamapper() + "updateInsaMap", insaMap);
	}

	/**
	 * @Description  : 프로필 파일경로 등록
	 * @author       : lakhyun.kim
	 * @since        : 2018. 07. 10
	 * @return       : void
	 */
	public void updateFilename(InsaVO insaVO){
		getSqlSession().update(getInsamapper() + "updateFilename", insaVO);
	}

	/**
	 * @Description  : 인사 수정
	 * @author       : lakhyun.kim
	 * @since        : 2018. 05. 30
	 * @return       : int
	 */
	public int updateInsa(InsaVO insaVO){
		int resultInt = 0;
		resultInt = getSqlSession().update(getInsamapper() + "updateInsa", insaVO);
		return resultInt;
	}

	/**
	 * @Description  : 인사 수정(밸류베스트용 - 밸류베스트 자체관리빼고 나머지 가져오는 정보 업데이트)
	 * @author       : yumi.jeon
	 * @since        : 2020. 05. 18
	 * @return       : int
	 */
	public int updateVmarkBestInsa(HashMap<String, String> insaMap){
		int resultInt = 0;
		resultInt = getSqlSession().update(getInsamapper() + "updateVmarkBestInsa", insaMap);
		return resultInt;
	}

	/**
	 * @Description  : 인사 삭제
	 * @author       : lakhyun.kim
	 * @since        : 2018. 06. 04
	 * @return       : int
	 */
	public int deleteInsa(InsaVO insaVO){
		int resultInt = 0;
		resultInt = getSqlSession().delete(getInsamapper() + "deleteInsa", insaVO);
		return resultInt;
	}
	
	/*********************************************************** 인사마감 ****************************************************/		
	/**
	 * @brief 인사마감목록조회 
	 * @param InsaVO
	 * @return List<InsaVO>
	 */
	public List<Map<String, String>> selectMonthInsaList(InsaVO insaVO){
		List<Map<String, String>> reulstList = new ArrayList<Map<String, String>>();
		reulstList = getSqlSession().selectList(getInsamapper() + "selectMonthInsaList", insaVO);
		return reulstList;
	}
	
	/**
	 * @Description  : 인사마감 사원정보 조회
	 * @author       : yumi.jeon
	 * @since        : 2019. 02. 27
	 * @return       : Map
	 */
	public Map selectMonthEmpView(InsaVO insaVO){
		Map resultMap = new HashMap();
		resultMap = getSqlSession().selectOne(getInsamapper() + "selectMonthEmpView", insaVO);
		return resultMap;
	}
	
	/**
	 * @Description  : 인사마감 수정
	 * @author       : yumi.jeon
	 * @since        : 2019. 02. 27
	 * @return       : int
	 */
	public int updateMonthInsa(InsaVO insaVO){
		int resultInt = 0;
		resultInt = getSqlSession().update(getInsamapper() + "updateMonthInsa", insaVO);
		return resultInt;
	}
	
	/**
	 * @Description  : 인사마감 정보 엑셀다운로드
	 * @author       : yumi.jeon
	 * @since        : 2019. 02. 27
	 * @return       : void
	 */
	public void selectMonthInsaListExcel(InsaVO insaVO , excelHandler2 eh){
		getSqlSession().select(getInsamapper() + "selectMonthInsaList", insaVO,eh);
	}
	

	/**
	 * @Description  : 인사 삭제
	 * @author       : lakhyun.kim
	 * @since        : 2018. 06. 04
	 * @return       : int
	 */
	public int deleteMonthInsa(InsaVO insaVO){
		int resultInt = 0;
		resultInt = getSqlSession().delete(getInsamapper() + "deleteMonthInsa", insaVO);
		return resultInt;
	}
	
	/**
	 * @Description  : 인사관리 해촉 처리
	 * @author       : yumi.jeon
	 * @since        : 2019. 03. 08
	 * @return       : int
	 */
	public int updateeInsaDismissal(InsaVO insaVO){
		int resultInt = 0;
		resultInt = getSqlSession().update(getInsamapper() + "updateeInsaDismissal", insaVO);
		return resultInt;
	}
	
	/**
	 * @Description  : 인사마감 해촉 처리
	 * @author       : yumi.jeon
	 * @since        : 2019. 03. 08
	 * @return       : int
	 */
	public int updateeMonthInsaDismissal(InsaVO insaVO){
		int resultInt = 0;
		resultInt = getSqlSession().update(getInsamapper() + "updateeMonthInsaDismissal", insaVO);
		return resultInt;
	}
	
	  

	/** 
	 * @Description 인사 발령 후 인사 정보 수정
	 * @param insaVO
	 */
	public void insaAppointment(InsaVO insaVO) {
		getSqlSession().update(getInsamapper() + "insaAppointment", insaVO);
	}
	
	/*********************************************************** 평가인사 생성 ****************************************************/	
	/**
	 * @brief 평가인사목록조회 
	 * @param InsaVO
	 * @return List<InsaVO>
	 */
	public List<InsaVO> selectMonthEvalInsaList(InsaVO insaVO){
		List<InsaVO> reulstList = new ArrayList<InsaVO>();
		if(CommUtil.isNotEmpty(insaVO.getPage())) {
			Integer TOTAL = getSqlSession().selectOne(getInsamapper() + "selectMonthEvalInsaListCount", insaVO);
			insaVO.setTOTAL(TOTAL);
			insaVO.setPageOffset(CommUtil.getPageOffset(insaVO.getPage(), insaVO.getPageSize()));
			
			reulstList = getSqlSession().selectList(getInsamapper() + "selectMonthEvalInsaListPaging", insaVO);
		}else {
			reulstList = getSqlSession().selectList(getInsamapper() + "selectMonthEvalInsaList", insaVO);			
		}
		
		return reulstList;
	}
	
	/**
	 * @Description  : 평가인사 마감 수정
	 * @author       : wonhyeok.choi
	 * @since        : 2020. 12. 11
	 * @return       : int
	 */
	public int updateMonthEvalInsaList(InsaVO insaVO){
		int resultInt = 0;
		resultInt = getSqlSession().update(getInsamapper() + "updateMonthEvalInsaList", insaVO);
		return resultInt;
	}
	
	
	/**
	 * @Description  : 평가인사 마감 리스트 엑셀다운로드
	 * @author       : wonheyok.choi	
	 * @since        : 2020. 12. 04
	 * @return       : void
	 */
	public void selectMonthEvalInsaListExcel(InsaVO insaVO , excelHandler2 eh){
		getSqlSession().select(getInsamapper() + "selectMonthEvalInsaList", insaVO,eh);
	}
	/*********************************************************** 평가인사 생성 ****************************************************/	
	
	/**
	 * @Description  : 사원 조회 DB분배 대상자 기준  조회
	 * @author       : lakhyun.kim
	 * @since        : 2019. 04. 30
	 * @return       : List<InsaVO>
	 */
	public List<InsaVO> selectEmpSchDbTargetList(InsaVO insaVO) {
		return getSqlSession().selectList(getInsamapper() + "selectEmpSchDbTargetList",insaVO);
	}
	
	
	/**
	 * @Description  : 인사 사원 조회 DB분배 동의 대상자 기준  조회
	 * @author       : lakhyun.kim
	 * @since        : 2019. 04. 30
	 * @return       : List<InsaVO>
	 */
	public List<InsaVO> selectDbTargetSchList(InsaVO insaVO) {
		return getSqlSession().selectList(getInsamapper() + "selectDbTargetSchList", insaVO);
	}

	public void deleteInsaInfo(InsaVO insaVO) {
		getSqlSession().delete(getInsamapper() + "deleteInsaInfo", insaVO);
	}

	public void deleteInsaWithoutScd(InsaVO insaVO) {
		getSqlSession().delete(getInsamapper() + "deleteInsaWithoutScd", insaVO);
	}

	/**
	 * @Description  : 주민번호로 사원 단건 조회
	 * @author       : KIMDONGUK
	 * @since        : 2019. 06. 05
	 * @return       : Map<String, String>
	 */
	public Map<String, String> selectEmpView2(InsaVO insaVO) {
		Map<String, String> resultMap = new HashMap<String, String>();
		
		resultMap = getSqlSession().selectOne(getInsamapper() + "selectEmpView2", insaVO);
		
		return resultMap;
	}

	/**
	 * @Description  : 핸드폰번호 중복 조회
	 * @author       : lakhyun.kim
	 * @since        : 2019. 09. 04
	 * @return       : Map<String, String>
	 * @param        : InsaVO
	 */
	public int selectEmpView3Count(InsaVO insaVO) {
		int resultInt = 0;
		resultInt = getSqlSession().selectOne(getInsamapper() + "selectEmpView3Count", insaVO);
		return resultInt;
	}
	/**
	 * @Description  : 핸드폰번호로 사원 단건 조회
	 * @author       : lakhyun.kim
	 * @since        : 2019. 09. 04
	 * @return       : Map<String, String>
	 * @param        : InsaVO
	 */
	public Map<String, String> selectEmpView3(InsaVO insaVO) {
		Map<String, String> resultMap = new HashMap<String, String>();
		resultMap = getSqlSession().selectOne(getInsamapper() + "selectEmpView3", insaVO);
		return resultMap;
	}
	
	/**
	 * @Description 아이디 확인 및 인증번호 생성 및 발송
	 * @Author taeyoon.kim 
	 * @Date 2019. 12. 03
	 * @param parametervo
	 * @return InsaVO
	 * @throws Exception
	 */
	public int selectEmpCount(InsaVO paramvo) throws Exception {
		int result = 0;
		try {
			result = getSqlSession().selectOne(getInsamapper() + "selectEmpCount", paramvo);
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw e;
		}
		return result;
	}
	
	/**
	 * @Description 아이디 확인 및 인증번호 생성 및 발송
	 * @Author taeyoon.kim 
	 * @Date 2019. 12. 03
	 * @param parametervo
	 * @return InsaVO
	 * @throws Exception
	 */
	public String getUserAuthId(InsaVO paramvo) throws Exception {
		String result = "";
		try {
			result = getSqlSession().selectOne(getInsamapper() + "getUserAuthId", paramvo);
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw e;
		}
		return result;
	}
	
	/**
	 * @Description  : 밸류마크 ERP 인사 데이터
	 * @author       : yumi.jeon
	 * @since        : 2020. 03. 03
	 * @return       : List<HashMap<String, String>>
	 */
	public List<HashMap<String, String>> selectVmarkEpmDatamapping(){
		List<HashMap<String, String>> reulstList = new ArrayList<HashMap<String, String>>();
		reulstList = getSqlSessionOracle().selectList(getInsamapper() + "selectVmarkEpmDatamapping");
		return reulstList;
	}
	
	/**
	 * @Description  : 밸류마크 베스트금융 ERP 인사 데이터
	 * @author       : yumi.jeon
	 * @since        : 2020. 05. 06
	 * @return       : List<HashMap<String, String>>
	 */
	public List<HashMap<String, String>> selectVmarkBestEpmDatamapping(){
		List<HashMap<String, String>> reulstList = new ArrayList<HashMap<String, String>>();
		reulstList = getSqlSessionOracle().selectList(getInsamapper() + "selectVmarkBestEpmDatamapping");
		return reulstList;
	}
	
	/**
	 * @Description  : ITX명세서 팀장권한일때 현재 ERP인사 기준 하위 조직들에 있는 사번 가져오기
	 * @author       : yumi.jeon
	 * @since        : 2020. 08. 27
	 * @return       : List<HashMap<String, String>>
	 */
	public String selectPaymentEmpcdListStr(InsaVO insaVO){
		String reulstStr = "";
		reulstStr = getSqlSession().selectOne(getInsamapper() + "selectPaymentEmpcdListStr", insaVO);
		return reulstStr;
	}
	
	/**
	 * @Description  : 인사 상세정보 리쿠르팅 가져오기
	 * @author       : bosung.kim
	 * @since        : 2020. 12. 15
	 * @return       : List<HashMap<String, String>>
	 */
	public List<Map<String, Object>> selectRecruitList(InsaVO insaVO) {
		return getSqlSession().selectList(getInsamapper() + "selectRecruitList", insaVO);
	}
}
