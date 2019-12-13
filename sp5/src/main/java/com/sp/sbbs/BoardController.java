package com.sp.sbbs;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sp.common.MyUtil;
import com.sp.member.SessionInfo;

@Controller("sbbs.boardController")
public class BoardController {

	@Autowired
	private BoardService service;

	@Autowired
	private MyUtil myUtil;

	@RequestMapping(value = "/sbbs/list")
	public String list(@RequestParam(value = "page", defaultValue = "1") int current_page,
			@RequestParam(defaultValue = "0") int group, @RequestParam(defaultValue = "all") String condition,
			@RequestParam(defaultValue = "") String keyword, HttpServletRequest req, Model model) throws Exception {

		String cp = req.getContextPath();

		int rows = 10;
		int offset = (current_page - 1) * rows;
		if (offset < 0)
			offset = 0;

		if (req.getMethod().equalsIgnoreCase("GET")) { // GET 방식인 경우
			keyword = URLDecoder.decode(keyword, "utf-8");
		}

		Map<String, Object> map = new HashMap<>();

		map.put("group", group);
		if (keyword != null && keyword != "") {
			map.put("condition", condition);
			map.put("keyword", keyword);
		}

		int dataCount = service.dataCount(map);

		int total_page = myUtil.pageCount(rows, dataCount);

		if (dataCount == 0)
			total_page = 0;

		// 다른 사람이 자료를 삭제하여 전체 페이지수가 변화 된 경우
		if (total_page < current_page)
			current_page = total_page;

		map.put("offset", offset);
		map.put("rows", rows);

		List<Board> list = service.listBoard(map);

		// 리스트의 번호
		int listNum, n = 0;
		for (Board dto : list) {
			listNum = dataCount - (offset + n);
			dto.setListNum(listNum);
			n++;
		}

		String query = "";
		String listUrl = cp + "/sbbs/list?group=" + group;
		String articleUrl = cp + "/sbbs/article?group=" + group + "&page=" + current_page;
		if (keyword.length() != 0) {
			query = "condition=" + condition + "&keyword=" + URLEncoder.encode(keyword, "utf-8");
		}

		if (query.length() != 0) {
			listUrl += "&" + query;
			articleUrl += "&" + query;
		}

		String paging = myUtil.paging(current_page, total_page, listUrl);

		Map<String, Object> map2 = new HashMap<String, Object>();
		map2.put("level", "group");

		List<Board> groupList = service.listCategory(map2);

		model.addAttribute("group", group);

		model.addAttribute("condition", condition);
		model.addAttribute("keyword", keyword);
		model.addAttribute("list", list);
		model.addAttribute("dataCount", dataCount);
		model.addAttribute("page", current_page);
		model.addAttribute("total_page", total_page);
		model.addAttribute("paging", paging);
		model.addAttribute("articleUrl", articleUrl);

		model.addAttribute("groupList", groupList);

		return ".sbbs.list";
	}

	@RequestMapping(value = "/sbbs/created", method = RequestMethod.GET)
	public String createdForm(Model model) throws Exception {

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("level", "group");

		List<Board> list = service.listCategory(map);

		model.addAttribute("listCategory", list);
		model.addAttribute("mode", "created");
		return ".sbbs.created";
	}

	@RequestMapping(value = "/sbbs/created", method = RequestMethod.POST)
	public String createdSubmit(Board dto, HttpSession session) throws Exception {

		SessionInfo info = (SessionInfo) session.getAttribute("member");

		try {
			dto.setUserId(info.getUserId());
			service.insertBoard(dto);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "redirect:/sbbs/list";
	}

	@RequestMapping(value = "/sbbs/listsubcategory", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> listSubCategory(@RequestParam int categoryNum) throws Exception {
		Map<String, Object> map = new HashMap<>();
		map.put("level", "sub");
		map.put("parent", categoryNum);

		List<Board> list = service.listCategory(map);

		Map<String, Object> model = new HashMap<>();
		model.put("list", list);

		return model;
	}

	@RequestMapping(value = "/sbbs/article")
	public String article(@RequestParam int num, @RequestParam(defaultValue = "1") int page, @RequestParam int group,
			@RequestParam(defaultValue = "all") String condition, @RequestParam(defaultValue = "") String keyword,
			Model model) throws Exception {

		keyword = URLDecoder.decode(keyword, "utf-8");

		String query = "page=" + page + "&group=" + group;
		if (keyword.length() != 0) {
			query += "&condition=" + condition + "&keyword=" + URLEncoder.encode(keyword, "UTF-8");
		}

		// 해당 레코드 가져 오기
		Board dto = service.readBoard(num);
		if (dto == null)
			return "redirect:/sbbs/list?" + query;

		service.updateHitCount(num);

		dto.setContent(myUtil.htmlSymbols(dto.getContent()));

		Map<String, Object> map2 = new HashMap<>();
		map2.put("group", group);
		map2.put("num", num);

		Board preReadDto = service.preReadBoard(map2);

		Board nextReadDto = service.nextReadBoard(map2);

		model.addAttribute("num", num);
		model.addAttribute("dto", dto);
		model.addAttribute("page", page);
		model.addAttribute("query", query);
		model.addAttribute("preReadDto", preReadDto);
		model.addAttribute("nextReadDto", nextReadDto);

		return ".sbbs.article";
	}

	@RequestMapping(value = "/sbbs/update", method = RequestMethod.GET)
	public String updateForm(Model model, @RequestParam int num) throws Exception {

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("level", "group");

		List<Board> list = service.listCategory(map);
		
		Board dto = service.readBoard(num);
		
		Map<String, Object> map2 = new HashMap<>();
		map2.put("level", "sub");
		map2.put("parent", dto.getGroupCategoryNum());

		List<Board> list2 = service.listCategory(map2);
		
		
		
		model.addAttribute("subListCategory", list2);
		model.addAttribute("dto", dto);
		model.addAttribute("listCategory", list);
		model.addAttribute("mode", "update");
		return ".sbbs.created";
	}
	
	@RequestMapping(value = "/sbbs/update", method = RequestMethod.POST)
	public String updateSubmit(Board dto, HttpSession session) throws Exception {
	try {
			service.updateBoard(dto);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "redirect:/sbbs/list";
	}
	
}
