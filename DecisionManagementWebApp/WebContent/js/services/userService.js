/**
 * service for user information
 */
var userServices = angular.module('userServices', ['ngResource']);

userServices.factory('Users', ['$resource', 
                                   function($resource){
                                     return $resource(serverAddress + '/DecisionManagement/rest/user/', {}, {
                                       query: {method:'GET', params:{}, isArray:true}
                                     });
                                   }]);

