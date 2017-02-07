/**
 * Created by MARCOS on 31/01/2017.
 */

var app = angular.module('learnerApp',['ngRoute']);


app.controller('mainCtrl', ['$scope', '$http', function($scope, $http){
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