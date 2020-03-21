// query1 : find users whose hometown citys the specified city. 

function find_user(city, dbname){
    db = db.getSiblingDB(dbname);

    var result = db.users.find({"hometown.city": city}, {user_id: 1, _id: 0});
    
    var results = [];
    for (var i = 0; i < result.length(); i++) {
    	results[i] = result[i]["user_id"];
    }
    // See test.js for a partial correctness check.  
    // The result will be an array of integers. The order does not matter.                                                               
    return results;
}
