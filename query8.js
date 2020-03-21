// query 8: Find the city average friend count per user using MapReduce
// Using the same terminology in query6, we are asking you to write the mapper,
// reducer and finalizer to find the average friend count for each city.


var city_average_friendcount_mapper = function() {
  emit(this.hometown.city, {"num": this.friends.length, "length": 1});
};

var city_average_friendcount_reducer = function(key, values) {
  var sum = 0;
    var num = 0;

    for (var i = 0; i < values.length; i++) {
    	sum += values[i]["num"];
    	num += values[i]["length"];
    }
  return {"num": sum, "length": num};
};

var city_average_friendcount_finalizer = function(key, reduceVal) {
  // We've implemented a simple forwarding finalize function. This implementation 
  // is naive: it just forwards the reduceVal to the output collection.
  // Feel free to change it if needed.
  var ret = reduceVal["num"] / reduceVal["length"];
  return ret;
}
