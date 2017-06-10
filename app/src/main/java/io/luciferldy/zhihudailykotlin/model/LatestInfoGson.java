package io.luciferldy.zhihudailykotlin.model;

import java.util.List;

/**
 * Created by Lucifer on 2017/6/9.
 */

public class LatestInfoGson {

    /**
     * date : 20170609
     * stories : [{"images":["https://pic4.zhimg.com/v2-abd2c1329d59c539f0566d9f078f4f07.jpg"],"type":0,"id":9466213,"ga_prefix":"060910","title":"老板打算用自己的名字给企业命名，我对公司未来充满信心"},{"images":["https://pic3.zhimg.com/v2-1e44f2b3a8424bf07dc4b1deae9c2032.jpg"],"type":0,"id":9464826,"ga_prefix":"060909","title":"今年，苹果设计奖选出了这 12 个好用又好看的 app"},{"images":["https://pic4.zhimg.com/v2-f049740a25d635540dfeeab566a879a3.jpg"],"type":0,"id":9465956,"ga_prefix":"060908","title":"平行世界里那个不爱念书的自己，会和现在差多少？"},{"images":["https://pic2.zhimg.com/v2-432247ad7ce50b54ed664c5c453bfc71.jpg"],"type":0,"id":9465285,"ga_prefix":"060907","title":"地铁是怎么修建的？"},{"images":["https://pic4.zhimg.com/v2-2272664156df223e3235cdd0865c4eab.jpg"],"type":0,"id":9460835,"ga_prefix":"060907","title":"做好职业生涯规划，房贷可能是一个最佳参照物"},{"images":["https://pic1.zhimg.com/v2-b98f34eccc332df4d745a6711fe58eb8.jpg"],"type":0,"id":9465654,"ga_prefix":"060907","title":"「裸贷」事件很糟糕，把学生贷款全部禁掉是个好主意吗？"},{"images":["https://pic1.zhimg.com/v2-b5629af844d6fa022602d58cbcff0b98.jpg"],"type":0,"id":9465275,"ga_prefix":"060906","title":"瞎扯 · 如何正确地吐槽"}]
     * top_stories : [{"image":"https://pic3.zhimg.com/v2-912ed77e8effec5588070848cd5a48c2.jpg","type":0,"id":9465654,"ga_prefix":"060907","title":"「裸贷」事件很糟糕，把学生贷款全部禁掉是个好主意吗？"},{"image":"https://pic1.zhimg.com/v2-4570557b705b203015dc5299fceddeb8.jpg","type":0,"id":9465393,"ga_prefix":"060817","title":"苹果要下架《王者荣耀》，这谣言越传越离谱"},{"image":"https://pic2.zhimg.com/v2-ad1943993e47946de67283ab6cf211b1.jpg","type":0,"id":9465265,"ga_prefix":"060815","title":"热水烫一下小饭馆的餐具，还真不只是心理安慰"},{"image":"https://pic3.zhimg.com/v2-5965c7148d45acd3aeeb1f6ade16f13e.jpg","type":0,"id":9464356,"ga_prefix":"060807","title":"早知道这些知识点，今年高考作文至少能多写 500 字"},{"image":"https://pic2.zhimg.com/v2-8ecb983ba1cf78a630bb99a206e66519.jpg","type":0,"id":9462294,"ga_prefix":"060710","title":"现在开始，世界上最大的 AR 平台是苹果"}]
     */

    private String date;
    private List<StoriesBean> stories;
    private List<TopStoriesBean> top_stories;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<StoriesBean> getStories() {
        return stories;
    }

    public void setStories(List<StoriesBean> stories) {
        this.stories = stories;
    }

    public List<TopStoriesBean> getTop_stories() {
        return top_stories;
    }

    public void setTop_stories(List<TopStoriesBean> top_stories) {
        this.top_stories = top_stories;
    }

    public static class StoriesBean {
        /**
         * images : ["https://pic4.zhimg.com/v2-abd2c1329d59c539f0566d9f078f4f07.jpg"]
         * type : 0
         * id : 9466213
         * ga_prefix : 060910
         * title : 老板打算用自己的名字给企业命名，我对公司未来充满信心
         */

        private int type;
        private int id;
        private String ga_prefix;
        private String title;
        private List<String> images;

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getGa_prefix() {
            return ga_prefix;
        }

        public void setGa_prefix(String ga_prefix) {
            this.ga_prefix = ga_prefix;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public List<String> getImages() {
            return images;
        }

        public void setImages(List<String> images) {
            this.images = images;
        }
    }

    public static class TopStoriesBean {
        /**
         * image : https://pic3.zhimg.com/v2-912ed77e8effec5588070848cd5a48c2.jpg
         * type : 0
         * id : 9465654
         * ga_prefix : 060907
         * title : 「裸贷」事件很糟糕，把学生贷款全部禁掉是个好主意吗？
         */

        private String image;
        private int type;
        private int id;
        private String ga_prefix;
        private String title;

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getGa_prefix() {
            return ga_prefix;
        }

        public void setGa_prefix(String ga_prefix) {
            this.ga_prefix = ga_prefix;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}
