// query6 : Find the Average friend count per user for users
//
// Return a decimal variable as the average user friend count of all users
// in the users document.

function find_average_friendcount(dbname){
  db = db.getSiblingDB(dbname)
  // TODO: return a decimal number of average friend count
  var result = db.users.find({}, {user_id: 1, friends: 1, _id: 0});
    
  var count = 0;
  for (var i = 0; i < result.length(); i++) {
    	count += result[i]["friends"].length;
  }
  var ans = count / result.length();
  return ans;
}
