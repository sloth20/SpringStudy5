package com.sp.sbbs;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sp.common.FileManager;
import com.sp.common.dao.CommonDAO;
import com.sp.common.dao.MyBatisDaoImpl;

@Service("sbbs.boardService")
public class BoardServiceImpl implements BoardService {

	@Autowired
	private CommonDAO dao;

	@Override
	public void insertBoard(Board dto) throws Exception {
		try {

			dao.insertData("sbbs.insertBoard", dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}

	}

	@Override
	public List<Board> listBoard(Map<String, Object> map) {
		List<Board> list = null;

		try {
			list = dao.selectList("sbbs.listBoard", map);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	@Override
	public int dataCount(Map<String, Object> map) {
		int result = -1;

		try {
			result = dao.selectOne("sbbs.dataCount", map);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	@Override
	public Board readBoard(int num) {
		Board board = null;

		try {
			board = dao.selectOne("sbbs.readBoard", num);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return board;
	}

	@Override
	public void updateHitCount(int num) throws Exception {
		try {
			dao.updateData("sbbs.updateHitCount", num);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public Board preReadBoard(Map<String, Object> map) {
		Board board = null;

		try {
			board = dao.selectOne("sbbs.preReadBoard", map);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return board;
	}

	@Override
	public Board nextReadBoard(Map<String, Object> map) {
		Board board = null;

		try {
			board = dao.selectOne("sbbs.nextReadBoard", map);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return board;
	}

	@Override
	public void updateBoard(Board dto) throws Exception {

		try {
			dao.updateData("sbbs.updateBoard", dto);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void deleteBoard(Map<String, Object> map) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void insertReply(Reply dto) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public List<Reply> listReply(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteReply(Map<String, Object> map) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void insertCategory(Map<String, Object> map) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateCategory(Map<String, Object> map) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public List<Board> listCategory(Map<String, Object> map) {
		List<Board> list = new ArrayList<Board>();

		try {
			list = dao.selectList("sbbs.listCategory", map);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	@Override
	public void deleteCategory(int categoryNum) throws Exception {
		// TODO Auto-generated method stub

	}

}
