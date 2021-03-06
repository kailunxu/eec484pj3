// find the oldest friend for each user who has a friend. 
// For simplicity, use only year of birth to determine age, if there is a tie, use the one with smallest user_id
// return a javascript object : key is the user_id and the value is the oldest_friend id
// You may find query 2 and query 3 helpful. You can create selections if you want. Do not modify users collection.
//
//You should return something like this:(order does not matter)
//{user1:userx1, user2:userx2, user3:userx3,...}

function oldest_friend(dbname){
    db = db.getSiblingDB(dbname);
    var results = {};
    var user = db.users.find();
    var userage = {};
    for (var i = 0; i < user.length(); ++i) {
        userage[user[i]["user_id"]] = user[i]["YOB"];
    }

    db.users.aggregate([
        {$project: {user_id: 1, friends: 1, _id: 0}},
        {$unwind: "$friends"},
        {$out: "flat_users"}
    ]);
    for (var i = 0; i < user.length(); ++i) {
        var oldest_age = 1000000;
        var smallest_id = 1000000;
        db.flat_users.find({user_id : user[i]["user_id"]}).forEach(
            function(myDoc) {
                if (userage[myDoc.friends] < oldest_age || 
                    (userage[myDoc.friends] == oldest_age && myDoc.friends < smallest_id)) {
                    oldest_age = userage[myDoc.friends];
                    smallest_id = myDoc.friends;
                }
            }
        )
        db.flat_users.find({friends : user[i]["user_id"]}).forEach(
          function(myDoc) {
              if (userage[myDoc.user_id] < oldest_age || 
                  (userage[myDoc.user_id] == oldest_age && myDoc.user_id < smallest_id)) {
                  oldest_age = userage[myDoc.user_id];
                  smallest_id = myDoc.user_id;
              }
          }
        )
        if (oldest_age != 1000000) {
            results[user[i]["user_id"]] = smallest_id;
        }
    }
    return results
}
