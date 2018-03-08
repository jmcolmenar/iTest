/**
 * Created by MARCOS on 31/01/2017.
 */

// Angular JS application
var app = angular.module("learnerApp",['ngRoute']);

// Service to get the user profile
app.factory("currentUserProfile",['$http', '$q', function($http, $q) {
    return function() {
        var userProfileData = $http.get('/api/user/getUserProfile');
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
            templateUrl: '/user/partial/changePassword',
            controller: 'changePassCtrl'
        })
        .when('/userprofile',{
            templateUrl: '/user/partial/userProfile',
            controller: 'userProfileCtrl',
            resolve: {
                currentProfile : ['currentUserProfile', function (currentUserProfile) {
                    return currentUserProfile();
                }]
            }
        })
        .when('/subject',{
            templateUrl: '/learner/partial/subject',
            controller: 'subjectCtrl'
        })
        .otherwise({
            redirectTo: '/'
        });
}]);

// Main controller
app.controller("mainCtrl", ['$scope', '$http', '$window', function($scope, $http, $window){

    // The function to executes when the page has been loaded
    $scope.init = function(){
        // Request to get the full name of user
        $http.get('/api/user/getFullName').success(function(response){
            // Set the full name of user
            $scope.fullName = response.fullName;
        }).error(function(response){
            // Set the full name with "Error" message
            $scope.fullName = 'Error...';
        });

        // Request to get the courses list of user
        $http.get('/api/learner/getCourses').success(function(response){
            if(response.hasError){
                // TODO: Show error modal

                // Set an empty courses list
                $scope.courseList = {};
            }else{
                // Set the variable with the courses
                $scope.courseList = response.coursesList;
            }
        }).error(function(response){
            // TODO: Show error modal

            // Set an empty courses list
            $scope.courseList = {};
        });
    };

    // Call to "logout" get request to exit of application
    $scope.exitApplication = function(){
        // Hide confirmation modal
        $("#exitConfirmationModal").modal("hide");

        // Redirect to "Logout" url
        $window.location.href = '/logout'
    };

    // Show the modal with the exit confirmation
    $scope.showExitConfirmation = function(){
        $("#exitConfirmationModal").modal("show");
    };

    // Set the selected subject
    $scope.setSelectedSubject = function(subject){
        // Set the GroupId of selected subject to search the exams
        $scope.selectedGroupId = subject.groupId;
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
            var changePasswordRequest = {
                oldPassword : $scope.changePasswordData.oldPassword,
                newPassword : $scope.changePasswordData.newPassword,
                repeatPassword : $scope.changePasswordData.repeatPassword,
            };
            $http.post('/api/user/changePassword', changePasswordRequest, {
                headers : {
                    "content-type" : "application/json"
                }
            }).success(function(response) {
                // Check if has an error in the change password process
                if(response.hasError){
                    // Shows the error message in the change password process
                    $scope.showProcessError = true;
                    $scope.processErrorMessage = response.errorMessage;

                    // Show the modal with the error
                    $("#errorModal").modal("show");
                }else{
                    // Shows the model with iformation message when the password has been changed successfully
                    $("#successfullyModal").modal("show");
                }

                // Clear the change password fields
                $scope.changePasswordData = {};

            }).error(function(response) {
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
    // The identifier of languages
    const SPANISH_ID = 0;
    const ENGLISH_ID = 1;

    // Select the language button with the current language of user
    if(currentProfile.userProfileData.languageId == SPANISH_ID){
        $("#spanishButton").addClass("active");
    }else if (currentProfile.userProfileData.languageId == ENGLISH_ID){
        $("#englishButton").addClass("active");
    }

    // Function to set the language buttons state
    $scope.setButtonState = function (event) {
        $("#spanishButton").removeClass("active");
        $("#englishButton").removeClass("active");

        var clickedButton = event.currentTarget;

        $("#" + clickedButton.id).addClass("active");
    };

    // Function to get the identifier of selected language button
    var getLanguageIdFromButtons = function () {
        if($("#spanishButton").hasClass("active")){
            return SPANISH_ID;
        }else if($("#englishButton").hasClass("active")){
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

        var updateUserProfileRequest = {
            name : $scope.profile.name,
            lastName : $scope.profile.lastname,
            email : $scope.profile.email,
            dni : $scope.profile.dni,
            languageId : getLanguageIdFromButtons()
        };

        // Post request to update the user profile
        $http.post('/api/user/updateUserProfile', updateUserProfileRequest, {
            headers : {
                "content-type" : "application/json"
            }
        }).success(function(response) {
            if(response.hasError){
                // Hide confirmation modal
                $("#confirmationModal").modal("hide");

                // Shows the error modal
                $("#errorModal").modal("show");
            }else{
                // Hide confirmation modal
                $("#confirmationModal").modal("hide");

                // Shows the static modal with successful message
                $("#successfullyModal").modal({backdrop: "static"});
            }
        }).error(function(response) {
            // Hide confirmation modal
            $("#confirmationModal").modal("hide");

            // Shows the error modal
            $("#errorModal").modal("show");
        })
    };
}]);

// Subject management controller
app.controller("subjectCtrl", ['$scope', '$http', function($scope, $http){

    // Prepare te request to get the exams by the Group Id of selected subject
    var getExamsInfoRequest = {
        groupId : $scope.selectedGroupId
    };

    // Get the done exams of selected subject
    $http.post('/api/learner/getExamsInfo', getExamsInfoRequest, {
        headers : {
            "content-type" : "application/json"
        }
    }).success(function(response) {
        if(response.hasError){
            // TODO: Shows an error modal

            // Set empty subject and exams info
            $scope.subject = {};
            $scope.doneExams = {};
        }else{
            // Set the list of done exams
            $scope.subject = response.subject;
            $scope.availableExamsList = response.availableExamsList;
            $scope.nextExamsList = response.nextExamsList;
            $scope.doneExamsList = response.doneExamsList;
        }
    }).error(function(response) {
        // TODO: Shows an error modal

        // Error retrieving the exams of selected subject
        $scope.showRequestError = true;
    });

    // Function to active the clicked button and deactive the others
    $scope.clickExamsButton = function($event){
        $("#available-exams-button").removeClass("active");
        $("#next-exams-button").removeClass("active");
        $("#done-exams-button").removeClass("active");

        var clickedButton = $event.currentTarget;
        $("#"+clickedButton.id).addClass("active");
    };

    // Functions to check the activated button
    $scope.isAvailableExamsButtonActived = function(){
        return $("#available-exams-button").hasClass("active");
    };
    $scope.isNextExamsButtonActived = function(){
        return $("#next-exams-button").hasClass("active");
    };
    $scope.isDoneExamsButtonActived = function(){
        return $("#done-exams-button").hasClass("active");
    };

    // Function to set the current exam in order to show the extra info in the modal
    $scope.setCurrentExamExtraInfo = function (exam) {
        $scope.currentExamExtraInfo = exam;
    }

}]);
