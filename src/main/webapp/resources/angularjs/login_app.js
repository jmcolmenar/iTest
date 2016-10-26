/**
 * Created by MARCOS on 17/10/2016.
 */
var app = angular.module('login',[]);
app.controller('loginCtrl', function($scope, $http, $window){
    // Set the variables to inital value
    $scope.showFormLogin = false;

    // Function to check if there is an authenticated user
    $scope.checkUser = function(){
        // Get request to check the authentication
        $http.get('checkAuthentication').success(function(response){
            // check if there is an authenticated user
            if(response == true){
                // Call Action "redirect" to login controller
                $window.location.href = '/redirect'
            }else{
                // Show form login
                $scope.showFormLogin = true;
            }
        });
    };
});