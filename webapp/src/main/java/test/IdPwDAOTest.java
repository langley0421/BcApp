package test;

import dao.IdPwDAO;
import dto.IdPw;

public class IdPwDAOTest {
	public static void main(String[] args) {
		testIsLoginOK1(); // ユーザーが見つかる場合のテスト
		testIsLoginOK2(); // ユーザーが見つからない場合のテスト
	}

	// ユーザーが見つかる場合のテスト
	public static void testIsLoginOK1() {
		IdPwDAO dao = new IdPwDAO();
		if (dao.isLoginOK(new IdPw("id", "password"))) {
			System.out.println("testIsLoginOK1：テストが成功しました");
		} else {
			System.out.println("testIsLoginOK1：テストが失敗しました");
		}
	}

	// ユーザーが見つからない場合のテスト
	public static void testIsLoginOK2() {
		IdPwDAO dao = new IdPwDAO();
		if (!dao.isLoginOK(new IdPw("id", "pass"))) {
			System.out.println("testIsLoginOK2：テストが成功しました");
		} else {
			System.out.println("testIsLoginOK2：テストが失敗しました");
		}
	}
}
