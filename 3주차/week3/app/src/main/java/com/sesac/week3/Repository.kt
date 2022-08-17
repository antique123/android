package com.sesac.week3

import com.sesac.week3.models.Goods
import com.sesac.week3.models.Post

object Repository {
    fun getGoods(): MutableList<Goods> {
        return mutableListOf(
            Goods(
                mutableListOf("https://img6.yna.co.kr/etc/inner/KR/2021/05/25/AKR20210525169600003_02_i_P2.jpg"),
                "1 (미개봉)정품 휘센 에어컨 팔아요",
                "가양역·3분전",
                "10000",
                "1 (미개봉)정품 휘센 에어컨 팔아요" + System.currentTimeMillis().toString()
            ),
            Goods(
                mutableListOf("https://upload.wikimedia.org/wikipedia/commons/thumb/2/27/Floor_fan.jpg/1200px-Floor_fan.jpg"),
                "2 (미개봉)정품 휘센 에어컨 팔아요",
                "가양역·3분전",
                "20000",
                "2 (미개봉)정품 휘센 에어컨 팔아요" + System.currentTimeMillis().toString()
            ),
            Goods(
                mutableListOf("https://www.dailysecu.com/news/photo/201908/63029_54314_99.jpg"),
                "3 (미개봉)정품 휘센 에어컨 팔아요",
                "가양역·3분전",
                "30000",
                "3 (미개봉)정품 휘센 에어컨 팔아요" + System.currentTimeMillis().toString()
            ),
            Goods(
                mutableListOf("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTgM-q0KPcEILetlZ184iQxRTRFi8B6i5ffMA&usqp=CAU"),
                "4 (미개봉)정품 휘센 에어컨 팔아요",
                "가양역·3분전",
                "40000",
                "4 (미개봉)정품 휘센 에어컨 팔아요" + System.currentTimeMillis().toString()

            ),
            Goods(
                mutableListOf("https://img.kr.news.samsung.com/kr/wp-content/uploads/2020/02/%EC%82%BC%EC%84%B1-%EA%B0%A4%EB%9F%AD%EC%8B%9C-Z-%ED%94%8C%EB%A6%BD-%ED%86%B0%EB%B8%8C%EB%9D%BC%EC%9A%B4-%EC%97%90%EB%94%94%EC%85%98-2-1.jpg"),
                "5 (미개봉)정품 휘센 에어컨 팔아요",
                "가양역·3분전",
                "50000",
                "5 (미개봉)정품 휘센 에어컨 팔아요" + System.currentTimeMillis().toString()
            )
        )
    }

    fun getPost(): MutableList<Post> {
        val posts = mutableListOf<Post>()
        posts.add(Post("동네질문", "Q.", "화곡역부근 골프레슨 잘하는 선생님 계실까요?", arrayListOf()))
        posts.add(Post("동네질문", "Q.", "혹시 염창역 근처에 어렸을 때 먹었던 피카츄 돈까스 파는곳이 있을까요? 갑자기 땡기네요", arrayListOf()))
        posts.add(Post("동네질문", "Q.", "동네에 노을 잘보이는 곳 있을까요? 집근처엔 건물에 가려져서 넘 아쉽더라구요 ㅎㅎ.. 발산 마곡 가양 염창 등촌 화곡 다 괜찮아요", arrayListOf()))
        posts.add(Post("동네질문", "Q.", "혹시 염창역 근처에 어렸을 때 먹었던 피카츄 돈까스 파는곳이 있을까요? 갑자기 땡기네요", arrayListOf()))
        posts.add(Post("동네질문", "Q.", "강서 홈플러스 근처 PCR 검사 가능한 병원 아시는 분 추천좀 부탁드려요ㅠ 아버지 병원진료로 급해서요..", arrayListOf()))
        posts.add(Post("동네질문", "Q.", "염창역 부근에 사는 30대중후반 남자인데요, 테니스를 배워보고 싶은데 장비부터 아무것도 준비된 것이 없어.. 여쭤봅니다. 가까운데(염창역 기준)", arrayListOf()))
        posts.add(Post("동네질문", "Q.", "혹시 염창역 근처에 어렸을 때 먹었던 피카츄 돈까스 파는곳이 있을까요? 갑자기 땡기네요", arrayListOf()))
        posts.add(Post("동네질문", "Q.", "강서 홈플러스 근처 PCR 검사 가능한 병원 아시는 분 추천좀 부탁드려요ㅠ 아버지 병원진료로 급해서요..", arrayListOf()))
        posts.add(Post("동네질문", "Q.", "화곡역부근 골프레슨 잘하는 선생님 계실까요?", arrayListOf()))

        return posts
    }
}