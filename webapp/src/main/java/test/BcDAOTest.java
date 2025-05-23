package test;

import java.util.List;

import dao.BcDAO;
import dto.Bc;

public class BcDAOTest {
	public static void showAllData(List<Bc> cardList) {
		for (Bc card : cardList) {
			System.out.println("番号：" + card.getNumber());
			System.out.println("氏名：" + card.getName());
			System.out.println("住所：" + card.getAddress());
			System.out.println();
		}
	}

	public static void main(String[] args) {
		BcDAO dao = new BcDAO();

		// select()のテスト1
		System.out.println("---------- select()のテスト1 ----------");
		List<Bc> cardListSel1 = dao.select(new Bc(0, "", "東京"));
		BcDAOTest.showAllData(cardListSel1);

		// select()のテスト2
		System.out.println("---------- select()のテスト2 ----------");
		List<Bc> cardListSel2 = dao.select(new Bc(0, "", ""));
		BcDAOTest.showAllData(cardListSel2);

		// insert()のテスト
		System.out.println("---------- insert()のテスト ----------");
		Bc insRec = new Bc(0, "栃木三郎", "栃木県宇都宮市塙田1-1-20");
		if (dao.insert(insRec)) {
			System.out.println("登録成功！");
			List<Bc> cardListIns = dao.select(new Bc(0, "", ""));
			BcDAOTest.showAllData(cardListIns);
		} else {
			System.out.println("登録失敗！");
		}

		// update()のテスト
		System.out.println("---------- update()のテスト ----------");
		List<Bc> cardListUp = dao.select(new Bc(0, "栃木三郎", ""));
		Bc upRec = cardListUp.get(0);
		upRec.setName("日光五郎");
		if (dao.update(upRec)) {
			System.out.println("更新成功！");
			cardListUp = dao.select(new Bc(0, "", ""));
			BcDAOTest.showAllData(cardListUp);
		} else {
			System.out.println("更新失敗！");
		}

		// delete()のテスト
		System.out.println("---------- delete()のテスト ----------");
		List<Bc> cardListDel = dao.select(new Bc(0, "日光五郎", ""));
		Bc delRec = cardListDel.get(0);
		if (dao.delete(delRec)) {
			System.out.println("削除成功！");
			cardListDel = dao.select(new Bc(0, "", ""));
			BcDAOTest.showAllData(cardListDel);
		} else {
			System.out.println("削除失敗！");
		}
	}
}
