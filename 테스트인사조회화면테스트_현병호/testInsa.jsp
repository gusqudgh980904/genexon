<!DOCTYPE html>
<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/resources/common/jstl-tld.jsp" %>
<html>
<head>
<meta charset="UTF-8">
<script type="text/javascript">
let treeDataSource;

$(document).ready(function() {
	$("#SRCH_EMP_VALUE").val("${InsaVO.SRCH_EMP_VALUE}");
	var height2 = window.innerHeight - (55+34+24+$('#search').height());
	$('#treeview').css("height", height2 + "px");
	
	// 에프씨네스트는 정렬순서 조직, 직책 순서
	if("${User.MB_ID}" == "FCN") {
		$("#SORT_COLUMN").val("snmpath3, jikchk_sort_no, ent_ymd DESC, seq DESC");
	} else {
		$("#SORT_COLUMN").val("ent_ymd DESC, seq DESC");
	}
	
	//화면 최초로딩시 조회처리
    $("#SRCH_EMPSTA").data("kendoDropDownList").bind("dataBound", srch);
 	
 	// [조회]
 	$('#search').bind('click', function(e){
 		srch();
 	});
 	
 	// 조회기간시작일 
 	$("#SRCH_TERM_START_VALUE").kendoDatePicker({
		format : "yyyy-MM-dd"
	});
	$('#SRCH_TERM_START_VALUE').kendoMaskedTextBox({
		mask : "0000-00-00"
	})
 	// 조회기간종료일
 	$("#SRCH_TERM_END_VALUE").kendoDatePicker({
		format : "yyyy-MM-dd"
	});
	$('#SRCH_TERM_END_VALUE').kendoMaskedTextBox({
		mask : "0000-00-00"
	})
	
	//시작일자 종료일자 관련
 	genexon.compareSrchTerm("SRCH_TERM_START_VALUE", "SRCH_TERM_END_VALUE");
 	
 	
 	// 엑셀다운로드
	$('#bt_excel_download').bind('click', function(e){
		excelDown();
	});

    
	// 소속 X 버튼
    genexon.addInitButton('SRCH_SNM', ['SRCH_SNM', 'SRCH_SCD']);
}); 

// [조회]
function srch() {
	var grid = $("#grid").data("kendoGrid");

	grid.dataSource.options.transport.read.data = {
	    JSON_STRING : genexon.getSearchParameterToJsonString(),
		SRCH_BANK : $('#SRCH_BANK').val(),
		SRCH_EMPSTA : $('#SRCH_EMPSTA').val(),
		SRCH_EMP_NM : $('#SRCH_EMP_NM').val()
	};
	    
	grid.pager.page(1);
}


// 양식다운로드
function downloadSample(){
	location.href = "/insa/insa/getExcelFormat.do";
}

//엑셀다운로드
function excelDown(){

	var data = {
			SRCH_BANK : $('#SRCH_BANK').val(),
			SRCH_EMPSTA : $('#SRCH_EMPSTA').val(),
			SRCH_EMP_NM : $('#SRCH_EMP_NM').val()
	};
	
	genexon.excelDown("#excelForm", data);
}

// 인사 상세정보
function goRegiPop(emp_cd){
	genexon.PopWindowOpen({
		pID : "insaDetail",
		pTitle : "인사 상세정보",
		pURL : "/insa/insa/insaDetail.pop?${_csrf.parameterName}=${_csrf.token}",
		data: { emp_cd : emp_cd },
	    pWidth : "1400",
	    pHeight : "750",
		position : {top:50, left:100},
		pModal : true
	});
}

/*마스터 복사*/
function copy_erp() {
	var date = new Date();
	var comym = genexon.getYyyymm(date, "");
	genexon.confirm("확인창", "이전에 생성된 전월/당월 인사데이타가 삭제 됩니다!\n재 적용 하시겠습니까?", function(result) {
	
		if (result) {
	
			var ds = genexon.initKendoUI_dataSource({
				mode : "CRUD",
				transport : {
					create : {
						url : ""
					}
				},
				schema : {
					data : "results",
					model : {
						id : "com_ym",
						fields : {
							com_ym : {},
							cl_step_cd : {}
						}
					}
				},
				sync : function(e) {
					srch();
				},
				requestStart : function(e) {
					kendo.ui.progress($("body"), true);
				},
				requestEnd : function(e) {
					kendo.ui.progress($("body"), false);
				}
			});
	
			ds.add(new kendo.data.Model({
				com_ym : comym,
				TABLE_TYPE : "01"
			}));
			ds.sync();
		}
	});
}

$(window).load(function(e){
	$(".resizegrid").find(".k-grid-content,.k-grid-content-locked").each(function() {
		var windowHeight = $(window).height();
		var top = $(this).offset().top;

		var result = windowHeight - top - 67; 
		$(this).css('height', result);
		
		if($(this).attr("class") == "k-grid-content-locked") {
			$(this).css('min-height', result - 19);
		}else {
			$(this).css('min-height', result);
		}
	});
})
</script>
</head>
<body>
<input type="hidden" name="SRCH_ORG_OPEN_GBN" id="SRCH_ORG_OPEN_GBN" value="Y"/>
<input type="hidden" name="SORT_COLUMN" id="SORT_COLUMN" value="ent_ymd DESC, seq DESC"/>
<input type="hidden" name="resource_url" id="resource_url" value="${menuRole.resource_url}"/>
<form id="excelForm" name="excelForm" method="post" action="/insa/getInsaTestListExcel.ajax?${_csrf.parameterName}=${_csrf.token}"></form>

<div>
			<tr>
				<td>
					<input type="text" class="kddl" name="SRCH_EMPSTA" id="SRCH_EMPSTA" ddcode="EMPSTA" optionLabel="재직구분"/>
					<input type="text" class="k-textbox" name="SRCH_EMP_NM" id="SRCH_EMP_NM"  style="background:#F8F8F8" placeholder="사원번호/사원명"/>
					<input type="text" class="kddl" name="SRCH_BANK" id="SRCH_BANK" ddcode="BANK" optionLabel="은행"/>
						<button class="k-primary btn_sty02" id="search" >조회</button>
					<button class="k-primary btn_sty03" id="bt_excel_download">엑셀다운로드</button>
				</td>	
			</tr>
</div>			
<div id="grid" class="resizegrid"></div>
<!-- grid -->

		<script  type="text/javascript">
		
		/**
		 * 데이터 소스설정
		 * 그룹코드 그리드 url 및 스키마모델 정의
		 **/
		var ds = {
			transport : {
				read : {
					url : "/insa/getTestInsaList.ajax?${_csrf.parameterName}=${_csrf.token}",
					data: function() {
						return {
							JSON_STRING : genexon.getSearchParameterToJsonString(),
							SRCH_BANK : $('#SRCH_BANK').val(),
							SRCH_EMPSTA : $('#SRCH_EMPSTA').val()
						}
					}
				},
			},
			schema : {
				model : {
					id : "seq",
					fields : {
						emp_cd:{}, 					
 						emp_nm:{},
 						snmpath:{},
 						scd:{},
 						empsta:{},
 						jikgub:{},
 						jikchk:{},
 						ent_ymd:{},
 						gent_ymd:{},
 						end_ymd:{},
 						bank:{},
 						bk_id:{}
					}
				}
			},
			batch: true,
			serverPaging: true,
			requestEnd : function(e) {
				if (e.type != undefined && e.type != "read") {
					srch();
				}
			}
		};
		
		/**
		 * 그룹코드 그리드 필드 정의
		 **/
		var gridpt = {
				columns : [ {
					field : "emp_cd",
					title : "사원번호",
					width : 160,
					locked: true,
					attributes :{
						style : "text-align : center"
					},
				}, {
					field : "emp_nm",
					title : "사원명",
					locked: true,
					width : 220,
					attributes :{
						style : "text-align : center;"
					}
				}, {
					field : "snmpath",
					title : "조직명",
					width : 90,
					attributes :{
						style : "text-align : center"
					}
				}, {
					field : "empsta",
					title : "재직구분",
					width : 90,
					attributes :{
						style : "text-align : center"
					}
				}, {
					field : "jikgub_nm",
					title : "직급",
					width : 120,
					attributes :{
						style : "text-align : center"
					}
				}, {
					field : "jikchk_nm",
					title : "직책",
					width : 140,
					attributes :{
						style : "text-align : center"
					},
				}, {
					field : "ent_ymd",
					title : "입사일자",
					width : 80,
					attributes : {
						style : "text-align : center"
					}
				}, {
					field : "gent_ymd",
					title : "위촉일자",
					width : 100,
					attributes :{
						style : "text-align : center"
					}
				}, {
					field : "end_ymd",
					title : "퇴사/해촉일자",
					width : 100,
					attributes :{
						style : "text-align : center"
						}
					},{
					field:"bank",
					title:"은행",
					width:100,
					attributes :{
						style : "text-align : center"
					}
				},{
					field:"bk_id_masking",
					title:"계좌번호",
					width:100,
					attributes :{
						style : "text-align : center"
					}
					}],
			dataBound: function (e) {
				var in_autr_type = "${menuRole.in_autr_type}";
				var up_autr_type = "${menuRole.up_autr_type}";
				var de_autr_type = "${menuRole.de_autr_type}";
			    var gridRows = e.sender.tbody.find("tr");
				genexon.autrBtnHandler(in_autr_type, up_autr_type, de_autr_type, gridRows);
										
			 	var gridId = e.sender.element.context.id;
           		//gridContentIncision(gridId,20);
			},
			height: 455,
			selectable: "row",
			sortable: true,
			change : onChange,
			pageable : true
		};

		/**
		 *인라인 그리드 생성
		 **/
		genexon.initKendoUI_grid_inlineEdit("#grid", gridpt, ds);

		/**
		 * 그리드 클릭(체인지) 이벤트	
		 **/
		function onChange(arg) {

		}

		
		</script>
		
				
		
	</body>
</html>