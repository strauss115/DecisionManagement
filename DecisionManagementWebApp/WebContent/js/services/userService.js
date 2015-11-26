/**
 * service for user information
 */
var userServices = angular.module('userServices', ['ngResource']);

userServices.factory('Users', ['$resource',
                                   function($resource){
                                     return $resource('http://140.78.186.44:8181/DecisionManagement/rest/user/', {}, {
                                       query: {method:'GET', params:{eMail:'user1@u1.com'}, isArray:true}
                                     });
                                   }]);

