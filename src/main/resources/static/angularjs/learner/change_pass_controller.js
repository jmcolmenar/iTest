/**
 * Created by MARCOS on 01/02/2017.
 */

app.controller('changePassCtrl', ['$scope', '$http', function($scope, $http){

    // The change password data
    $scope.changePasswordData = {};

    // Function to perform login
    $scope.changePassword = function() {
        // Check the requeriments of fields to change the password


        $http.post('/learner/changePassword', $.param($scope.changePasswordData), {
            headers : {
                "content-type" : "application/x-www-form-urlencoded"
            }
        }).success(function(data) {
            // TODO: Change password has been changed, shows succesfully message


            // Clear the change password fields
            $scope.changePasswordData = {};

        }).error(function(data) {
            // TODO: Error changing the password, shows the error message

            // Clear the change password fields
            $scope.changePasswordData = {};
        })
    };

}]);