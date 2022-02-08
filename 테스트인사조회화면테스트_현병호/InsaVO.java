package kr.co.gnx.insa;

import kr.co.gnx.base.BaseVO;

public class InsaVO extends BaseVO {
	private String perno;
	private String perno_masking;//사원주민번호 마스킹
	private String emp_nme;
	private String telno;
	private String hpno;
	private String extno;
	private String email;
	private String insco_emp_cd_gbn;
	private String insco_emp_cd_gbn_nm;
	private String join_gbn;
	private String pay_gbn;
	private String rcrt_type;
	private String rcrt_typenm;
	private String re_emp_cd;
	private String re_emp;
	private String re_emp_nm;
	private String re_emp_snm;
	private String re_jikgub;
	private String lreg_ymd;
	private String nreg_ymd;
	private String vreg_ymd;
	private String vreg_no;	/* 변액등록번호 */
	private String areg_no;	/* 협회등록번호 */
	private String areg_yn;	/* 협회등록여부 */
	private String areg_yn_nm;	/* 협회등록여부 */
	private String ent_ymd;
	private String gent_ymd;
	private String apgent_ymd;
	private String end_ymd;
	private String out_ymd;
	private String tax_elecno;
	private String bank;
	private String bk_id;
	private String owner;
	private String owner_relation;
	private String owner_relationnm;
	private String file_name;
	private String login_id;
	private String appoint_flag;	//소속, 직책, 직급, 권한이 수정되었는지 체크하여 발령데이타에 추가 
	private String jikgub_nm;
	private String jikchk_nm;
	private String bank_nm;
	private String sort_type; // 정렬 타입
    private String[] emp_cd_arr;
    private String career_gbn;	// 경력여부
    private String career_gbn_nm;
    private String jikchk_sort_no;	// 직책 정렬순서
    private String corp_email_addr;	// 회사이메일
    
    // EXL 추가
    private String gain_ymd;  /* 취득일 */
    private String lost_ymd;  /* 상실일 */	

	//the-top 추가
    private String reentry_ymd;		//재입사일
    private String birth;		//생년월일
	
    // DB 신청용
    private String apply_count;                /* 신청 DB             BIGINT        */
    private String assignment_count;	// 배정 DB
    private String assignment_rate;		// 배정율
    
    // nike 추가
    private String lreg_insco_cd;		//생보등록사
    private String nreg_insco_cd;		//손보등록사
    private String lreg_insco_nm;		//생보등록사
    private String nreg_insco_nm;		//손보등록사
    private String start_sale_dtm;		//손보등록사
    
    //AFG 추가
    private String out_reason; //해촉사유
    private String out_reason_nm;
    private String office_nm;
    private String lreg_out_ymd;		//생보말소일
    private String nreg_out_ymd;		//손보말소일
    

	// KAC 가동율포함여주 구분
    private String movable_ratio_gbn;

    private String settlement_gbn;
    private String settlement_gbn_nm;
    private String appoint_ymd;
     
    private String earn_gbn;
    private String earn_gbn_nm;
    
    
    
	public String getRe_emp() {
		return re_emp;
	}

	public void setRe_emp(String re_emp) {
		this.re_emp = re_emp;
	}

	public String getLreg_out_ymd() {
		return lreg_out_ymd;
	}

	public void setLreg_out_ymd(String lreg_out_ymd) {
		this.lreg_out_ymd = lreg_out_ymd;
	}

	public String getNreg_out_ymd() {
		return nreg_out_ymd;
	}

	public void setNreg_out_ymd(String nreg_out_ymd) {
		this.nreg_out_ymd = nreg_out_ymd;
	}

	public String getStart_sale_dtm() {
		return start_sale_dtm;
	}

	public void setStart_sale_dtm(String start_sale_dtm) {
		this.start_sale_dtm = start_sale_dtm;
	}

	public String getOffice_nm() {
		return office_nm;
	}

	public void setOffice_nm(String office_nm) {
		this.office_nm = office_nm;
	}

	public String getOut_reason_nm() {
		return out_reason_nm;
	}

	public void setOut_reason_nm(String out_reason_nm) {
		this.out_reason_nm = out_reason_nm;
	}

	public String getOut_reason() {
		return out_reason;
	}

	public void setOut_reason(String out_reason) {
		this.out_reason = out_reason;
	}

	public String getGain_ymd() {
		return gain_ymd;
	}

	public void setGain_ymd(String gain_ymd) {
		this.gain_ymd = gain_ymd;
	}

	public String getLost_ymd() {
		return lost_ymd;
	}

	public void setLost_ymd(String lost_ymd) {
		this.lost_ymd = lost_ymd;
	}

	public String getAreg_yn_nm() {
		return areg_yn_nm;
	}

	public void setAreg_yn_nm(String areg_yn_nm) {
		this.areg_yn_nm = areg_yn_nm;
	}

	public String getSettlement_gbn_nm() {
		return settlement_gbn_nm;
	}

	public void setSettlement_gbn_nm(String settlement_gbn_nm) {
		this.settlement_gbn_nm = settlement_gbn_nm;
	}

	public String getSettlement_gbn() {
		return settlement_gbn;
	}

	public void setSettlement_gbn(String settlement_gbn) {
		this.settlement_gbn = settlement_gbn;
	}

	public String getPerno() {
		return perno;
	}

	public void setPerno(String perno) {
		this.perno = perno;
	}

	public String getPerno_masking() {
		return perno_masking;
	}

	public void setPerno_masking(String perno_masking) {
		this.perno_masking = perno_masking;
	}

	public String getEmp_nme() {
		return emp_nme;
	}

	public void setEmp_nme(String emp_nme) {
		this.emp_nme = emp_nme;
	}

	public String getTelno() {
		return telno;
	}

	public void setTelno(String telno) {
		this.telno = telno;
	}

	public String getHpno() {
		return hpno;
	}

	public void setHpno(String hpno) {
		this.hpno = hpno;
	}

	public String getExtno() {
		return extno;
	}

	public void setExtno(String extno) {
		this.extno = extno;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getInsco_emp_cd_gbn() {
		return insco_emp_cd_gbn;
	}

	public void setInsco_emp_cd_gbn(String insco_emp_cd_gbn) {
		this.insco_emp_cd_gbn = insco_emp_cd_gbn;
	}

	public String getInsco_emp_cd_gbn_nm() {
		return insco_emp_cd_gbn_nm;
	}

	public void setInsco_emp_cd_gbn_nm(String insco_emp_cd_gbn_nm) {
		this.insco_emp_cd_gbn_nm = insco_emp_cd_gbn_nm;
	}

	public String getJoin_gbn() {
		return join_gbn;
	}

	public void setJoin_gbn(String join_gbn) {
		this.join_gbn = join_gbn;
	}

	public String getPay_gbn() {
		return pay_gbn;
	}

	public void setPay_gbn(String pay_gbn) {
		this.pay_gbn = pay_gbn;
	}

	public String getRcrt_type() {
		return rcrt_type;
	}

	public void setRcrt_type(String rcrt_type) {
		this.rcrt_type = rcrt_type;
	}

	public String getRcrt_typenm() {
		return rcrt_typenm;
	}

	public void setRcrt_typenm(String rcrt_typenm) {
		this.rcrt_typenm = rcrt_typenm;
	}

	public String getRe_emp_cd() {
		return re_emp_cd;
	}

	public void setRe_emp_cd(String re_emp_cd) {
		this.re_emp_cd = re_emp_cd;
	}

	public String getRe_emp_nm() {
		return re_emp_nm;
	}

	public void setRe_emp_nm(String re_emp_nm) {
		this.re_emp_nm = re_emp_nm;
	}

	public String getRe_emp_snm() {
		return re_emp_snm;
	}

	public void setRe_emp_snm(String re_emp_snm) {
		this.re_emp_snm = re_emp_snm;
	}

	public String getRe_jikgub() {
		return re_jikgub;
	}

	public void setRe_jikgub(String re_jikgub) {
		this.re_jikgub = re_jikgub;
	}

	public String getLreg_ymd() {
		return lreg_ymd;
	}

	public void setLreg_ymd(String lreg_ymd) {
		this.lreg_ymd = lreg_ymd;
	}

	public String getNreg_ymd() {
		return nreg_ymd;
	}

	public void setNreg_ymd(String nreg_ymd) {
		this.nreg_ymd = nreg_ymd;
	}

	public String getVreg_ymd() {
		return vreg_ymd;
	}

	public void setVreg_ymd(String vreg_ymd) {
		this.vreg_ymd = vreg_ymd;
	}
	
	public String getVreg_no() {
		return vreg_no;
	}

	public void setVreg_no(String vreg_no) {
		this.vreg_no = vreg_no;
	}

	public String getAreg_no() {
		return areg_no;
	}

	public void setAreg_no(String areg_no) {
		this.areg_no = areg_no;
	}

	public String getAreg_yn() {
		return areg_yn;
	}

	public void setAreg_yn(String areg_yn) {
		this.areg_yn = areg_yn;
	}

	public String getEnt_ymd() {
		return ent_ymd;
	}

	public void setEnt_ymd(String ent_ymd) {
		this.ent_ymd = ent_ymd;
	}

	public String getGent_ymd() {
		return gent_ymd;
	}

	public void setGent_ymd(String gent_ymd) {
		this.gent_ymd = gent_ymd;
	}
	
	public String getApgent_ymd() {
		return apgent_ymd;
	}

	public void setApgent_ymd(String apgent_ymd) {
		this.apgent_ymd = apgent_ymd;
	}

	public String getEnd_ymd() {
		return end_ymd;
	}

	public void setEnd_ymd(String end_ymd) {
		this.end_ymd = end_ymd;
	}

	public String getOut_ymd() {
		return out_ymd;
	}

	public void setOut_ymd(String out_ymd) {
		this.out_ymd = out_ymd;
	}
	
	public String getTax_elecno() {
		return tax_elecno;
	}

	public void setTax_elecno(String tax_elecno) {
		this.tax_elecno = tax_elecno;
	}

	public String getBank() {
		return bank;
	}

	public void setBank(String bank) {
		this.bank = bank;
	}

	public String getBk_id() {
		return bk_id;
	}

	public void setBk_id(String bk_id) {
		this.bk_id = bk_id;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getOwner_relation() {
		return owner_relation;
	}

	public String getOwner_relationnm() {
		return owner_relationnm;
	}

	public void setOwner_relationnm(String owner_relationnm) {
		this.owner_relationnm = owner_relationnm;
	}

	public void setOwner_relation(String owner_relation) {
		this.owner_relation = owner_relation;
	}

	public String getFile_name() {
		return file_name;
	}

	public void setFile_name(String file_name) {
		this.file_name = file_name;
	}

	public String getLogin_id() {
		return login_id;
	}

	public void setLogin_id(String login_id) {
		this.login_id = login_id;
	}

	public String getAppoint_flag() {
		return appoint_flag;
	}

	public void setAppoint_flag(String appoint_flag) {
		this.appoint_flag = appoint_flag;
	}

	public String getJikgub_nm() {
		return jikgub_nm;
	}

	public void setJikgub_nm(String jikgub_nm) {
		this.jikgub_nm = jikgub_nm;
	}

	public String getJikchk_nm() {
		return jikchk_nm;
	}

	public void setJikchk_nm(String jikchk_nm) {
		this.jikchk_nm = jikchk_nm;
	}

	public String getBank_nm() {
		return bank_nm;
	}

	public void setBank_nm(String bank_nm) {
		this.bank_nm = bank_nm;
	}

	public String getReentry_ymd() {
		return reentry_ymd;
	}

	public void setReentry_ymd(String reentry_ymd) {
		this.reentry_ymd = reentry_ymd;
	}

	public String getBirth() {
		return birth;
	}

	public void setBirth(String birth) {
		this.birth = birth;
	}

	public String getApply_count() {
		return apply_count;
	}

	public void setApply_count(String apply_count) {
		this.apply_count = apply_count;
	}

	public String getAssignment_count() {
		return assignment_count;
	}

	public void setAssignment_count(String assignment_count) {
		this.assignment_count = assignment_count;
	}

	public String getAssignment_rate() {
		return assignment_rate;
	}

	public void setAssignment_rate(String assignment_rate) {
		this.assignment_rate = assignment_rate;
	}

	public String getSort_type() {
		return sort_type;
	}

	public void setSort_type(String sort_type) {
		this.sort_type = sort_type;
	}

	public String[] getEmp_cd_arr() {
		return emp_cd_arr;
	}

	public void setEmp_cd_arr(String[] emp_cd_arr) {
		this.emp_cd_arr = emp_cd_arr;
	}

	public String getCareer_gbn() {
		return career_gbn;
	}

	public void setCareer_gbn(String career_gbn) {
		this.career_gbn = career_gbn;
	}

	public String getCareer_gbn_nm() {
		return career_gbn_nm;
	}

	public void setCareer_gbn_nm(String career_gbn_nm) {
		this.career_gbn_nm = career_gbn_nm;
	}

	public String getJikchk_sort_no() {
		return jikchk_sort_no;
	}

	public void setJikchk_sort_no(String jikchk_sort_no) {
		this.jikchk_sort_no = jikchk_sort_no;
	}

	public String getLreg_insco_cd() {
		return lreg_insco_cd;
	}

	public void setLreg_insco_cd(String lreg_insco_cd) {
		this.lreg_insco_cd = lreg_insco_cd;
	}

	public String getNreg_insco_cd() {
		return nreg_insco_cd;
	}

	public void setNreg_insco_cd(String nreg_insco_cd) {
		this.nreg_insco_cd = nreg_insco_cd;
	}

	public String getLreg_insco_nm() {
		return lreg_insco_nm;
	}

	public void setLreg_insco_nm(String lreg_insco_nm) {
		this.lreg_insco_nm = lreg_insco_nm;
	}

	public String getNreg_insco_nm() {
		return nreg_insco_nm;
	}

	public void setNreg_insco_nm(String nreg_insco_nm) {
		this.nreg_insco_nm = nreg_insco_nm;
	}

	public String getMovable_ratio_gbn() {
		return movable_ratio_gbn;
	}

	public void setMovable_ratio_gbn(String movable_ratio_gbn) {
		this.movable_ratio_gbn = movable_ratio_gbn;
	}

	public String getAppoint_ymd() {
		return appoint_ymd;
	}

	public void setAppoint_ymd(String appoint_ymd) {
		this.appoint_ymd = appoint_ymd;
	}

	public String getCorp_email_addr() {
		return corp_email_addr;
	}

	public void setCorp_email_addr(String corp_email_addr) {
		this.corp_email_addr = corp_email_addr;
	}

	public String getEarn_gbn() {
		return earn_gbn;
	}

	public void setEarn_gbn(String earn_gbn) {
		this.earn_gbn = earn_gbn;
	}

	public String getEarn_gbn_nm() {
		return earn_gbn_nm;
	}

	public void setEarn_gbn_nm(String earn_gbn_nm) {
		this.earn_gbn_nm = earn_gbn_nm;
	}
	

}
