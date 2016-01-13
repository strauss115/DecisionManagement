/**
 * Controller for the 'showTimeline.html' - Timeline Report
 */

app.controller('TimelineController', [
		'$scope',
		'$sce',
		'$cookies',
		'$location',
		'DecisionsByTeam',
		function($scope, $sce, $cookies, $location, DecisionsByTeam) {
			
			$scope.decisionTimeLine =  $sce.trustAsHtml("Loading data...");
			
			$scope.colours = ["danger","warning","info","success", "primary"];
			$scope.icons = ["check","check-square-o","check-circle","check-square", "check-circle-o", "plus", "plus-circle", "plus-square", "star", "send", "magic"];
			

			$scope.niceDate = function(date){
				var d = new Date(date);
				var zero = d.getMonth() < 9 ? "0": ""; 
				return d.getDate() + "." + zero + (d.getMonth() + 1 ) + "." + d.getFullYear(); 
			}
			
			$scope.cycleColour = function(){
				$scope.colours.push($scope.colours[0]);
				$scope.colours.shift();
				return $scope.colours[0];
			}
			
			$scope.cycleIcon = function(){
				$scope.icons.push($scope.icons[0]);
				$scope.icons.shift();
				return $scope.icons[0];
			}
			
						
			$scope.buildTimelineHTML = function(inputJSON){
				var res = "";
				$.each(inputJSON, function(idx, item){
					// use any of font-awsome icons http://fortawesome.github.io/Font-Awesome/icons/
					// note, version 4.5 is not supported yet
					// if icon does not start with 'fa-', the prefix will be added 
					
					//console.log(item.icon.indexOf("fa-"));
					
					// adds a timeline-inverted class for odd entries to display item on right side of the timeline
					res += "<li" + (idx % 2 == 0 ? "" : " class=\"timeline-inverted\"")  + ">";
					//res += "<div class=\"timeline-badge " + $scope.colourAutocorrect(item.colour) + "\"><i class=\"fa " +  $scope.iconAutocorrect(item.icon) + "\"></i></div>";
					res += "<div class=\"timeline-badge "  + $scope.cycleColour() + "\"><i class=\"fa fa-" + $scope.cycleIcon() + "\"></i></div>";
					
					res += "<div class=\"timeline-panel\">";
					// add timeline heading
					res += "<div class=\"timeline-heading\">";
					res += "<h4 class=\"timeline-title\">" + item.name + "</h4>";
					// add date
					res += "<p><small class=\"text-muted\"><i class=\"fa fa-clock-o\"></i> " + $scope.niceDate(item.creationDate) + "  <i class=\"fa fa-user\"></i>  " + item.author + " </small></p></div>";
					// add time line description (decision comment)
					res += "<div class=\"timeline-body\"><p>" 
						
					if(item.influenceFactors.length > 0) {res +=  "<i class=\"fa fa-clock-o\"></i> " + item.influenceFactors.length + " influence factors. ";}	
					if(item.rationales.length > 0) {res +=  "<i class=\"fa fa-cog\"></i> " + item.rationales.length + " rationales. ";}	
					if(item.alternatives.length > 0) {res +=  "<i class=\"fa fa-clone\"></i> " + item.alternatives.length + " alternatives. ";}	
					if(item.consequences.length > 0) {res +=  "<i class=\"fa fa-external-link\"></i> " + item.consequences.length + " consequences. ";}	
					if(item.qualityAttributes.length > 0) {res +=  "<i class=\"fa fa-diamond\"></i> " + item.qualityAttributes.length + " quality attributes. ";}	
					if(item.relatedDecisions.length > 0) {res +=  "<i class=\"fa fa-exchange\"></i> " + item.relatedDecisions.length + " related decisions. ";}	
					if(item.responsibles.length > 0) {res +=  "<i class=\"fa fa-male\"></i> " + item.responsibles.length + " responsibles. ";}	
					if(item.documents.length > 0) {res +=  "<i class=\"fa fa-file-image-o\"></i> " + item.documents.length + " documents. ";}	
					
					
					res += "<br> "+ item.lastActivity + "</p></div></div></li>";
					
				});
				//console.log(res);
				return res;
		
			}
			
			DecisionsByTeam.get({}, function(data) {
				
				// var decisionTimeLine is synchrosnized with DIV tag in showTimeline.html template by 'ng-bind-html'
				 console.log(angular.toJson(data));
				$scope.decisionTimeLine =  $sce.trustAsHtml($scope.buildTimelineHTML(data)); // html snipplet must be tagged as trusted in order to be displayed
				
			}, function(error) {
				$scope.decisionTimeLine = "Sorry, an error has occured. Server could not be reached and seems to be offline. Please, try again later."
			});
		} ]);