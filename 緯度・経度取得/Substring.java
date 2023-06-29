import java.util.*;

public class Substring {
    
    /*
    double latitude1 = [拠点１の緯度]
    double longitude1 = [拠点１の経度]
    double latitude2 = [拠点２の緯度]
    double longitude2 = [拠点２の経度]
    */
    
    public static void main(String[] args) throws Exception {

        
        String postalCode = "9550011";
        
        String latlng = "lat/lng: (38.4840956,139.5319804)";
        
        System.out.println(latlng);
        
        // ==================== テストデータ
        //String input = "Hello, World!";
        //String start = ",";
        //String end = "!";
        
        // === 緯度取得
        String input = "lat/lng: (38.4840956,139.5319804)";
        String start = "(";
        String end = ",";
        
        String result = Substring(input, start, end);
        System.out.println(result); // 出力: 38.4840956
        
        
        // === 経度取得
        String input_02 = "lat/lng: (38.4840956,139.5319804)";
        String start_02 = ",";
        String end_02 = ")";
        
        String result_02 = Substring(input_02, start_02, end_02);
        System.out.println(result_02); // 出力: 139.5319804
        
    }
    
    /**
     *    開始文字と、終了文字を指定して、その間の文字列を取得
     *   
     */
    public static String Substring(String all_str, String start, String end) {
        
        int startIndex = all_str.indexOf(start);
        System.out.println("startIndex:::" + startIndex); // 5
        
        int endIndex = all_str.indexOf(end, startIndex + start.length());
        
        System.out.println("endIndex:::" + endIndex); // 12
        System.out.println("start.length():::" + start.length()); // 1
        
        return all_str.substring(startIndex + start.length(), endIndex);

}
    
    
    
    
}
