
// query 4: find user pairs (A,B) that meet the following constraints:
// i) user A is male and user B is female
// ii) their Year_Of_Birth difference is less than year_diff
// iii) user A and B are not friends
// iv) user A and B are from the same hometown city
// The following is the schema for output pairs:
// [
//      [user_id1, user_id2],
//      [user_id1, user_id3],
//      [user_id4, user_id2],
//      ...
//  ]
// user_id is the field from the users collection. Do not use the _id field in users.
  
function suggest_friends(year_diff, dbname) {
    db = db.getSiblingDB(dbname);
    var pairs = [];
    var result = db.users.find();
    for (var i = 0; i < result.length(); i++) {
        for (var j = 0; j < result.length(); j++) {
            if (result[i]["gender"] == "male" && result[j]["gender"] == "female") {
                if (result[i]["hometown"]["city"] == result[j]["hometown"]["city"] &&
                    result[i]["hometown"]["state"] == result[j]["hometown"]["state"] &&
                    result[i]["hometown"]["country"] == result[j]["hometown"]["country"]) {
                    if (Math.abs(result[i]["YOB"] - result[j]["YOB"]) < year_diff) {
                        if (result[i]["user_id"] < result[j]["user_id"]) {
                            if (result[i]["friends"].indexOf(result[j]["user_id"]) == -1) {
                                pairs.push([result[i]["user_id"], result[j]["user_id"]]);
                            }
                        } else {
                            if (result[j]["friends"].indexOf(result[i]["user_id"]) == -1) {
                                pairs.push([result[i]["user_id"], result[j]["user_id"]]);
                            }
                        }
                    }
                }
            }
        }
    }
    // TODO: implement suggest friends
    // Return an array of arrays.
    return pairs;
}
