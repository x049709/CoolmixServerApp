var axonmicro = angular.module('axonmicro', [ 'ngRoute' ]);

axonmicro.config(function($routeProvider, $httpProvider) {
	$routeProvider
	.when('/', {
		templateUrl : 'home.html',
		controller : 'home'
	})
	.when('/axonuser', {
		templateUrl : 'user.html',
		controller : 'axonuser'
	})
	.when('/axonuserlist', {
		templateUrl : 'userlist.html',
		controller : 'axonuserlist'
	})
	.when('/axonproduct', {
		templateUrl : 'product.html',
		controller : 'axonproduct'
	})
	.otherwise('/');

	$httpProvider.defaults.headers.common['X-Requested-With'] = 'XMLHttpRequest';

})

axonmicro.filter('dateToString', function($filter)
{
	return function(input)
	{
		if(input == null) { 
			return ""; 
		} 
		var dateString = $filter('date')(new Date(input), 'yyyy-MM-dd'); 
		return dateString;
	};
})

axonmicro.controller('rootcontroller', function($rootScope, $scope, $http, $location, $route) {
	
	$scope.selectedUser = null;

	$http.get('user').success(function(data) {
		if (data.name) {
			$rootScope.authenticated = true;
		} else {
			$rootScope.authenticated = false;
		}
	}).error(function() {
		$rootScope.authenticated = false;
	});

	$scope.credentials = {};

	$scope.logout = function() {
		$http.post('logout', {}).success(function() {
			$rootScope.authenticated = false;
			$location.path("/");
		}).error(function(data) {
			console.log("Logout failed")
			$rootScope.authenticated = false;
		});
	};
	
})

axonmicro.controller('home', function($scope, $http) {
	$http.get('resource/').success(function(data) {
		$scope.greeting = data;
	})
	
})

axonmicro.controller('axonproduct', function($scope, $http) {
	$http.get('axonuser/111').success(function(data) {
		$scope.user = data;
	})

})

axonmicro.controller('axonuser', function($scope, $http) {
	$http.get('axonuser/111').success(function(data) {
		$scope.user = data;
	})
})

axonmicro.controller('axonuserlist', function($scope, $http, $filter) {
	$scope.selectedUser = null;
	$scope.editSelectedUser = false;
    $scope.showUpdateOKMsg = null;
    $scope.showUpdateFailedMsg = null;

	$scope.currentDate = new Date();
	$http.get('axonuserlist/').success(function(data) {
		angular.forEach(data, function(datavalue, dob) {
			datavalue.dob = new Date(datavalue.dob);
		});
		$scope.userList = data;		
	})

	$scope.editUser = function(user) {
		var userDetail = angular.element($("#userDetailDiv"));
		var editUserDetail = angular.element($("#editUserDetailDiv"));
		$scope.editSelectedUser = true;
		$scope.selectedUser = user;
	    $scope.showUpdateOKMsg = null;
	    $scope.showUpdateFailedMsg = null;
	};
	
	$scope.addNewUser = function() {
	    $scope.showUpdateOKMsg = null;
	    $scope.showUpdateFailedMsg = null;
		$scope.editSelectedUser = false;
		var newUser = {};
		newUser.user_id = 0;
		newUser.fname = "";
		newUser.fname = "";
		newUser.lname = "";
		newUser.email = "";
		newUser.dob = new Date();
		newUser.ssn = "";
		newUser.emplid = "";
		$scope.selectedUser = newUser;
		
	};

	$scope.saveUser = function(in_user) {
		var userToSave = JSON.parse(angular.toJson(in_user));
		userToSave.dob = $filter('date') (userToSave.dob,'yyyy-MM-dd');
		var user = angular.toJson(in_user);
		//var user = JSON.stringify(in_user);
		
		$http({
		    url: 'axonuseradd/',
		    method: 'POST',
		    data: userToSave,
		    contentType: 'application/json'
		    }).success(function(response){
		        $scope.showUpdateOKMsg = "OK";
		        $scope.showUpdateFailedMsg = null;
		    }).error(function(error){
		        $scope.showUpdateOKMsg = null;
		        $scope.showUpdateFailedMsg = error;
		});
		
		$scope.selectedUser = null;
	};
	
})


