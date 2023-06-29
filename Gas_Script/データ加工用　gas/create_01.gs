function myFunction(fileId, sheetName) {

  var file = DriveApp.getFileById(fileId);
  var sheet = SpreadsheetApp.getActiveSpreadsheet().getSheetByName(sheetName);
  
  var content = file.getBlob().getDataAsString();
  var dataArray = content.split("\n");
  
  // === 1行目から書きこむ
  var columnToWrite = 1;

  // === ２列目から挿入
  for(var i = 1; i < dataArray.length; i++) {
    sheet.getRange(i + 1, columnToWrite).setValue(dataArray[i]);
  }

}

// ========= 実行（テキストファイルから、値取得）
myFunction("1XxvjZ3ZHH48PwYFntSoIh7kMgVWawSWs", "シート1");


/**
 *  ===　緯度・経度　取得
 */
function GeoCoder() {

  const START_ROW = 2;
  const FACILITY_COL = 1;
  const ADDRESS_COL = 2; // 住所名
  const LAT_COL = 3; // 緯度
  const LNG_COL = 4; // 経度

  var spreadsheet = SpreadsheetApp.getActiveSpreadsheet().getSheetByName("シート1");
  var lastrow = spreadsheet.getLastRow(); // 最終行取得

 // === 2　～　最終行までループ
  for(var i = START_ROW; i <= lastrow; i++) {

    // １行目の値を取得
    var facility = spreadsheet.getRange(i,FACILITY_COL).getValue();

    var geocoder = Maps.newGeocoder();
    geocoder.setLanguage('ja');

    var response = geocoder.geocode(facility);

    if(response['results'][0] != null) {

      // 緯度
      spreadsheet.getRange(i, LAT_COL).setValue(response['results'][0]['geometry']['location']['lat']);

      // 経度
      spreadsheet.getRange(i, LNG_COL).setValue(response['results'][0]['geometry']['location']['lng']);
    
      // 住所取得
      spreadsheet.getRange(i, ADDRESS_COL).setValue(response['results'][0]['formatted_address']);
    }


  }


}


// === CSV ファイル作成

function convertToCSV() {
  var sheet = SpreadsheetApp.getActiveSpreadsheet().getSheetByName('Sheet1'); // シート名を指定します
  var dataRange = sheet.getDataRange();
  var data = dataRange.getValues();
  
  var csvContent = '';
  
  for (var i = 0; i < data.length; i++) {
    var rowData = data[i];
    var row = [];
    
    // 必要な列のインデックスを指定します（例えば、A列とC列を取得する場合は[0, 2]とします）
    var columnIndices = [0, 2];
    
    for (var j = 0; j < columnIndices.length; j++) {
      var columnIndex = columnIndices[j];
      
      // セルの値を取得し、ダブルクォーテーションで囲みます
      var cellValue = rowData[columnIndex];
      var cellValueFormatted = '"' + cellValue + '"';
      
      row.push(cellValueFormatted);
    }
    
    // 行のデータをCSV形式に変換し、改行文字で連結します
    var rowCSV = row.join(',');
    
    csvContent += rowCSV + '\n';
  }
  
  // CSVファイルをダウンロードします
  var csvBlob = Utilities.newBlob(csvContent, 'application/octet-stream', 'file.csv');
  DriveApp.createFile(csvBlob);
  
  SpreadsheetApp.getUi().alert('CSVファイルが作成されました。');
}



