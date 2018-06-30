/**
 * Created by MARCOS on 17/10/2016.
 */
var app = angular.module('retrievepass',[]);
app.controller('mainCtrl',['$scope', '$http', '$window', function($scope, $http, $window){

    // Initialize the varibales to retrieve the password
    $scope.retrievePasswordData = {};
    $scope.retrievePasswordData.username = '';
    $scope.retrievePasswordData.email = '';

    // Function to retrieve the password
    $scope.retrievePassword = function () {

        // Check all fields are not null or empty
        if(!$scope.retrievePasswordData.username || !$scope.retrievePasswordData.username.trim()) {

            // Show the modal with the error when any field is empty
            $scope.errorType = "emptyFields";
            $("#errordModal").modal("show");

        }else{

            // Prepare the request object
            var retrievePasswordRequest = { username: $scope.retrievePasswordData.username };

            // Call to the server to retrieve the password of user
            $http.post('/api/user/retrievePassword', retrievePasswordRequest, {
                headers : {
                    'content-type' : 'application/json'
                }
            }).success(function(response) {
                // check if has an error retrieving the user password
                if(response.hasError){
                    // Check if the server returns an error
                    if(response.errorMessage){
                        $scope.errorType = "serverError";
                        $scope.serverError = response.errorMessage;
                    }else{
                        $scope.errorType = "error";
                    }

                    // Show modal error
                    $("#errordModal").modal("show");

                }else{
                    // Show the success modal
                    $("#successfullyRetrievePasswordModal").modal({backdrop: "static"});
                }
            }).error(function(response) {
                // Show the modal error
                $scope.errorType = "error";
                $("#errordModal").modal("show");
            })
        }
    };

    // Function to go to index page
    $scope.goToIndex = function () {
        // Redirect to index page
        $window.location.href = '/'
    };

    // Initialize the variables to change to new password
    $scope.changePasswordData = {};
    $scope.changePasswordData.newPass = '';
    $scope.changePasswordData.repeatPass = '';

    // Function to change the user password to new one
    $scope.changeNewPassword = function () {

        // Check all fields are not null or empty
        if(!$scope.changePasswordData.newPass || !$scope.changePasswordData.newPass.trim()
            || !$scope.changePasswordData.repeatPass || !$scope.changePasswordData.repeatPass.trim()) {

            // Show the modal with the error when any field is empty
            $scope.errorType = "emptyFields";
            $("#errordModal").modal("show");
        }else{

            // Prepare the request object
            var changeNewPasswordRequest = { newPassword: $scope.changePasswordData.newPass, repeatPassword: $scope.changePasswordData.repeatPass };

            // Call to the server to retrieve the password of user
            $http.post('/api/user/changeNewPassword', changeNewPasswordRequest, {
                headers : {
                    'content-type' : 'application/json'
                }
            }).success(function(response) {
                // check if has an error retrieving the user password
                if(response.hasError){
                    // Check if the server returns an error
                    if(response.errorMessage){
                        $scope.errorType = "serverError";
                        $scope.serverError = response.errorMessage;
                    }else{
                        $scope.errorType = "error";
                    }

                    // Show modal error
                    $("#errordModal").modal("show");

                }else{
                    // Show the success modal
                    $("#successfullyRetrievePasswordModal").modal({backdrop: "static"});
                }
            }).error(function(response) {
                // Show the modal error
                $scope.errorType = "error";
                $("#errordModal").modal("show");
            })
        }
    };

}]);