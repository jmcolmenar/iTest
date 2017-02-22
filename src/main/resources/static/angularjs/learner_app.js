/**
 * Created by MARCOS on 31/01/2017.
 */

// Angular JS application
var app = angular.module("learnerApp",['ngRoute']);

// Service to get the user profile
app.factory("currentUserProfile",['$http', '$q', function($http, $q) {
    return function() {
        var userProfileData = $http.get('/api/learner/getUserProfile');
        return $q.all([userProfileData]).then(function(results){
            return {
                userProfileData: results[0].data
            };
        });
    }
}]);

// Route provider configuration
app.config(['$routeProvider', function ($routeProvider){
    $routeProvider
        .when('/', {
            templateUrl: '/learner/partial/courses'
        })
        .when('/changepassword', {
            templateUrl: '/learner/partial/changePassword',
            controller: 'changePassCtrl'
        })
        .when('/userprofile',{
            templateUrl: '/learner/partial/userProfile',
            controller: 'userProfileCtrl',
            resolve: {
                currentProfile : ['currentUserProfile', function (currentUserProfile) {
                    return currentUserProfile();
                }]
            }
        });
}]);

// Main controller
app.controller("mainCtrl", ['$scope', '$http', function($scope, $http){
    // The routes of the partial views
    $scope.changePasswordViewRoute = '/learner/partial/changePassword';
    $scope.userProfileViewRoute = '/learner/partial/userProfile';

    // The function to executes when the page has been loaded
    $scope.init = function(){
        // Request to get the full name of user
        $http.get('/api/learner/getFullName').success(function(response){
            // Set the variable with the courses
            $scope.fullName = response.fullName;
        });

        // Request to get the courses list of user
        $http.get('/api/learner/getCourses').success(function(response){
            // Set the variable with the courses
            $scope.courseList = response;
        });
    };

}]);

// Change password controller
app.controller("changePassCtrl", ['$scope', '$http', function($scope, $http){

    // The change password data
    $scope.changePasswordData = {};

    // Function to perform login
    $scope.changePassword = function() {
        // For default he errors changing the password are not shown
        $scope.showEmptyFieldsError = false;
        $scope.showRequestError = false;
        $scope.showProcessError = false;

        // Check the all fields are not null or empty
        if(!$scope.changePasswordData.oldPassword || !$scope.changePasswordData.newPassword || !$scope.changePasswordData.repeatPassword){
            // Show the empty field error
            $scope.showEmptyFieldsError = true;

            // Show the modal with the error
            $("#errorModal").modal("show");
        }else{
            $http.post('/api/learner/changePassword', $.param($scope.changePasswordData), {
                headers : {
                    "content-type" : "application/x-www-form-urlencoded"
                }
            }).success(function(data) {
                // Check if has an error in the change password process
                if(data.isChanged){
                    // Shows the model with iformation message when the password has been changed successfully
                    $("#successfullyModal").modal("show");
                }else{
                    // Shows the error message in the change password process
                    $scope.showProcessError = true;
                    $scope.processErrorMessage = data.errorMessage;

                    // Show the modal with the error
                    $("#errorModal").modal("show");
                }

                // Clear the change password fields
                $scope.changePasswordData = {};

            }).error(function(data) {
                // Error changing the password, shows the error message
                $scope.showRequestError = true;

                // Show the modal with the error
                $("#errorModal").modal("show");

                // Clear the change password fields
                $scope.changePasswordData = {};
            })
        }
    };

}]);

// User profile controller
app.controller("userProfileCtrl", ['$scope', '$http', '$window', 'currentProfile', function($scope, $http, $window, currentProfile) {

    // The states of buttons for language
    const BUTTON_SELECTED_STATE = 'btn-language active';
    const BUTTON_NO_SELECTED_STATE = 'btn-language';

    // The identifier of languages
    const SPANISH_ID = 0;
    const ENGLISH_ID = 1;

    // The variables with the status of languages buttons
    $scope.spanishButtonState = '';
    $scope.englishButtonState = '';

    // Function to set the language buttons state
    $scope.setButtonState = function (langId) {
        if(langId == SPANISH_ID){
            $scope.spanishButtonState = BUTTON_SELECTED_STATE;
            $scope.englishButtonState = BUTTON_NO_SELECTED_STATE;
        }else if(langId == ENGLISH_ID){
            $scope.spanishButtonState = BUTTON_NO_SELECTED_STATE;
            $scope.englishButtonState = BUTTON_SELECTED_STATE;
        }
    };

    // Executes the function to shows the selected language
    $scope.setButtonState(currentProfile.userProfileData.languageId);

    // Function to get the identifier of selected language button
    var getLanguageIdFromButtons = function () {
        if($scope.spanishButtonState == BUTTON_SELECTED_STATE){
            return SPANISH_ID;
        }else if($scope.englishButtonState == BUTTON_SELECTED_STATE){
            return ENGLISH_ID;
        }
    };

    // Function to shows the confirmation modal
    $scope.showConfirmationModal = function () {
        $("#confirmationModal").modal("show");
    };

    // Function to redirect to home page
    $scope.goToIndexPage = function () {
        // The learner is redirected to the home page
        $window.location.href = '/learner/'
    };

    // Set the user profile data to shows in the form
    $scope.profile = {};
    $scope.profile.user = currentProfile.userProfileData.username;
    $scope.profile.name = currentProfile.userProfileData.name;
    $scope.profile.lastname = currentProfile.userProfileData.lastName;
    $scope.profile.email = currentProfile.userProfileData.email;
    $scope.profile.dni = currentProfile.userProfileData.dni;

    // Function to update the user profile
    $scope.updateUserProfile = function () {

        // Initialize of user profile model object
        var userProfileModel = {};
        userProfileModel.name = $scope.profile.name;
        userProfileModel.lastName = $scope.profile.lastname;
        userProfileModel.email = $scope.profile.email;
        userProfileModel.dni = $scope.profile.dni;
        userProfileModel.languageId = getLanguageIdFromButtons();

        // Post request to update the user profile
        $http.post('/api/learner/updateUserProfile', $.param(userProfileModel), {
            headers : {
                "content-type" : "application/x-www-form-urlencoded"
            }
        }).success(function(data) {
            // Hide confirmation modal
            $("#confirmationModal").modal("hide");

            // Shows the static modal with successful message
            $("#successfullyModal").modal({backdrop: "static"});
        }).error(function(data) {
            // Hide confirmation modal
            $("#confirmationModal").modal("hide");

            // Shows the error modal
            $("#errorModal").modal("show");
        })
    };
}]);
