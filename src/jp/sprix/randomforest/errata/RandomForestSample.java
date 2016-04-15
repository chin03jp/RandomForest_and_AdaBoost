package jp.sprix.randomforest.errata;

public class RandomForestSample {
	/**
	 * 木の数
	 */
	private int treeCnt = 0;

	/**
	 * 木の配列
	 */
	private RandomForestTree[] randomForestTreeArray = null;

	/**
	 * validationの結果を木の数だけ保存
	 */
	private int[][] validationResult = null;

	/**
	 * コンストラクタ
	 * 
	 * @param treeCnt
	 */
	public RandomForestSample(int treeCnt) {
		this.treeCnt = treeCnt;
		randomForestTreeArray = new RandomForestTree[treeCnt];
	}

	/**
	 * 学習して木の配列を作成する
	 * 
	 * @param sampleCase
	 *            全事例オブジェクト
	 * @param randomForestSampling
	 *            学習するサンプリング集合（事例の添字）
	 */
	public void train(RandomForestErrataCase sampleCase, RandomForestSampling randomForestSampling) {
		for (int i = 0; i < treeCnt; i++) {
			System.out.println((i + 1) + "本目");
			RandomForestTree tree = new RandomForestTree(i, randomForestSampling.getSampling(i),
					sampleCase);
			tree.makeTree();
			randomForestTreeArray[i] = tree;

			// Thread th = new Thread(tree);
			// th.start();
		}
	}

	/**
	 * クエリのバリデーションを行う
	 * 
	 * @param errataCase
	 */
	public void validation(RandomForestErrataCase errataCase) {
		// 木の数だけ結果を保持する
		validationResult = new int[treeCnt][];
		for (int i = 0; i < randomForestTreeArray.length; i++) {
			// 木
			validationResult[i] = randomForestTreeArray[i].validation(errataCase);
		}

		// 投票結果をsampleCaseオブジェクトに登録
		int[] votingResult = new int[errataCase.cases.length];
		for (int i = 0; i < votingResult.length; i++) {
			int votingCnt = 0;
			for (int j = 0; j < validationResult.length; j++) {
				votingCnt += validationResult[j][i];

			}
			// 正事例と負事例の率が同じ場合(レンジはtreecntから-treecntまで)
			// if (votingCnt > 0) {
			// 正事例の方に重みを置く
			if (votingCnt > 0) {
				votingResult[i] = 1;
			} else {
				votingResult[i] = -1;
			}
		}

		errataCase.setClassify(votingResult);
	}

	public int getTreeCnt() {
		return treeCnt;
	}

	public void setTreeCnt(int treeCnt) {
		this.treeCnt = treeCnt;
	}

	public RandomForestTree[] getRandomForestTreeArray() {
		return randomForestTreeArray;
	}

	public void setRandomForestTreeArray(RandomForestTree[] randomForestTreeArray) {
		this.randomForestTreeArray = randomForestTreeArray;
	}

}
