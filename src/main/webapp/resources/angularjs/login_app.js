/**
 * Created by MARCOS on 17/10/2016.
 */
var app = angular.module('login',[]);
app.controller('loginCtrl', function($scope, $http, $window){
    // Set the variables to inital value
    $scope.showFormLogin = false;
    $scope.showLoginError = false;
    $scope.credentials = {}

    // Function to check if there is an authenticated user
    $scope.checkUser = function(){
        // Get request to check the authentication
        $http.get('checkCurrentUser').success(function(response){
            // check if there is an authenticated user
            if(response.validUser){
                // Call Action "redirect" to login controller
                $window.location.href = '/redirect'
            }else{
                // Show form login
                $scope.showFormLogin = true;
            }
        });
    };

    $scope.login = function() {
        $http.post('login', $.param($scope.credentials), {
            headers : {
                "content-type" : "application/x-www-form-urlencoded"
            }
        }).success(function(data) {
            // Check if the logged iser is a valid user (With the needed Roles)
            if(data.validUser){
                // Call Action "redirect" to login controller
                $window.location.href = '/redirect'
            }else{
                // Show error message
                $scope.showLoginError = true;
            }
        }).error(function(data) {
            // Show error message
            $scope.showLoginError = true;
        })
    };
});