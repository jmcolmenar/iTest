/**
 * Created by MARCOS on 17/10/2016.
 */
var app = angular.module('login',[]);
app.controller('loginCtrl', function($scope, $http, $window){
    // Set the variables to inital value
    $scope.showFormLogin = false;
    $scope.showLoginError = false;
    $scope.showInvalidUserError = false;
    $scope.credentials = { username : '', password : '' };

    // Function to check if there is an authenticated user
    $scope.checkUser = function() {
        // Request to check the current session
        $http.post('/api/user/checkSession', {}, {
            headers : {
                'content-type' : 'application/json'
            }
        }).success(function(response) {
            // check if there is an authenticated user
            if(response.authorizedUser){
                // Call Action "redirect" to login controller
                $window.location.href = '/redirect'
            }else{
                // Show form login
                $scope.showFormLogin = true;
            }
        }).error(function(response) {
            // Show form login
            $scope.showFormLogin = true;
        })
    };

    // Function to perform login
    $scope.login = function() {
        $http.post('login', $.param($scope.credentials), {
            headers : {
                'content-type' : 'application/x-www-form-urlencoded'
            }
        }).success(function(data) {
            // Check if the logged user is a valid user (With the needed Roles)
            if(data.validUser){
                // Call Action "redirect" to login controller
                $window.location.href = '/redirect'
            }else{
                // Show invalid user error message
                $scope.showInvalidUserError = true;
                $scope.showLoginError = false;

                // Clear the typed credentials
                $scope.credentials = {};
            }
        }).error(function(data) {
            // Show login error message
            $scope.showLoginError = true;
            $scope.showInvalidUserError = false;

            // Clear the typed credentials
            $scope.credentials = {};
        })
    };
});