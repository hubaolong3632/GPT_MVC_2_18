//public class BingSearch extends ToolModel {
//
//    @Field(description = "bing搜索输入")
//    private String searchInput;
//
//    @Override
//    public void use() {
//        String url = "https://cn.bing.com/search?q=" + searchInput + "&aqs=edge.2.69i64i450l8.175106209j0j1&FORM=ANAB01&PC=HCTS";
//        Request request = new Request.Builder()
//                .url(url)
//                .build();
//        try (Response response = client.newCall(request).execute()) {
//            if (!response.isSuccessful()) {
//                return "未找到相关结果";
//            }
//            String html = response.body().string();
//            org.jsoup.nodes.Document document = Jsoup.parse(html);
//            Elements items = document.select("li.b_algo");
//            List<Map<String, String>> results = new ArrayList<>();
//            for (org.jsoup.nodes.Element item : items) {
//                Map<String, String> dataDict = new HashMap<>();
//                dataDict.put("title", item.select("div:nth-child(1) h2").text());
//                dataDict.put("link", item.select("div:nth-child(1) a").attr("href"));
//                dataDict.put("snippet", item.select("div:nth-child(2) p").text());
//                results.add(dataDict);
//
//                if (results.size() == 7) {
//                    break;
//                }
//            }
//            return results.toString();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public class Meta {
//        public static final String name = "get_bing_search_results";
//        public static final String description = "通过必应的智能搜索，可以更轻松地快速查找所需内容并获得奖励。";
//    }
//}