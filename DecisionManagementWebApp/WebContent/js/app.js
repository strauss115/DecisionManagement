var app = angular.module('decisionApp', ['ngRoute', 'ngCookies', 'ngResource', 'userServices', 'loginServices', 'teamServices', 'decisionServices', 'angularFileUpload']);
var serverAddress = "http://127.0.0.1:8181";



app.run(function ($rootScope, $cookies, $location) {
    $rootScope.$on("$locationChangeStart", function (event, next, current) {
        if (!$cookies['Token'] && $location.path() != "/login" ) {
            $location.path("/login");
            $("#loginErrorModal").modal();
        }
    });
});

app.directive('goDiagramMindMap', function () {
    return {
        restrict: 'E',
        template: '<div></div>', // just an empty DIV element
        replace: true,
        scope: {model: '=goModel'},
        link: function (scope, element, attrs) {
            var $ = go.GraphObject.make;
            var diagram = // create a Diagram for the given HTML DIV element
                    $(go.Diagram, element[0],
                            {
                                "nodeTemplate": $(go.Node, "Vertical",
                                        {selectionObjectName: "TEXT"},
                                $(go.TextBlock,
                                        {
                                            name: "TEXT",
                                            minSize: new go.Size(30, 15),
                                            editable: true
                                        },
                                // remember not only the text string but the scale and the font in the node data
                                new go.Binding("text", "text").makeTwoWay(),
                                        new go.Binding("scale", "scale").makeTwoWay(),
                                        new go.Binding("font", "font").makeTwoWay()),
                                        $(go.Shape, "LineH",
                                                {
                                                    stretch: go.GraphObject.Horizontal,
                                                    strokeWidth: 3, height: 3,
                                                    // this line shape is the port -- what links connect with
                                                    portId: "", fromSpot: go.Spot.LeftRightSides, toSpot: go.Spot.LeftRightSides
                                                },
                                        new go.Binding("stroke", "brush"),
                                                // make sure links come in from the proper direction and go out appropriately
                                                new go.Binding("fromSpot", "dir", function (d) {
                                                    return spotConverter(d, true);
                                                }),
                                                new go.Binding("toSpot", "dir", function (d) {
                                                    return spotConverter(d, false);
                                                })),
                                        // remember the locations of each node in the node data
                                        new go.Binding("location", "loc", go.Point.parse).makeTwoWay(go.Point.stringify),
                                        // make sure text "grows" in the desired direction
                                        new go.Binding("locationSpot", "dir", function (d) {
                                            return spotConverter(d, false);
                                        })
                                        ),
                                "nodeTemplate.selectionAdornmentTemplate": $(go.Adornment, "Spot",
                                        $(go.Panel, "Auto",
                                                // this Adornment has a rectangular blue Shape around the selected node
                                                $(go.Shape, {fill: null, stroke: "dodgerblue", strokeWidth: 3}),
                                                $(go.Placeholder, {margin: new go.Margin(4, 4, 0, 4)})
                                                ),
                                        // and this Adornment has a Button to the right of the selected node
                                        $("Button",
                                                {
                                                    alignment: go.Spot.Right,
                                                    alignmentFocus: go.Spot.Left,
                                                    click: addNodeAndLink  // define click behavior for this Button in the Adornment
                                                },
                                        $(go.TextBlock, "+", // the Button content
                                                {font: "bold 8pt sans-serif"})
                                                )
                                        ),
                                "nodeTemplate.contextMenu": $(go.Adornment, "Vertical",
                                        $("ContextMenuButton",
                                                $(go.TextBlock, "Bigger"),
                                                {click: function (e, obj) {
                                                        changeTextSize(obj, 1.1);
                                                    }}),
                                        $("ContextMenuButton",
                                                $(go.TextBlock, "Smaller"),
                                                {click: function (e, obj) {
                                                        changeTextSize(obj, 1 / 1.1);
                                                    }}),
                                        $("ContextMenuButton",
                                                $(go.TextBlock, "Bold/Normal"),
                                                {click: function (e, obj) {
                                                        toggleTextWeight(obj);
                                                    }}),
                                        $("ContextMenuButton",
                                                $(go.TextBlock, "Layout"),
                                                {
                                                    click: function (e, obj) {
                                                        var adorn = obj.part;
                                                        adorn.diagram.startTransaction("Subtree Layout");
                                                        layoutTree(adorn.adornedPart);
                                                        adorn.diagram.commitTransaction("Subtree Layout");
                                                    }
                                                }
                                        )
                                        ),
                                "linkTemplate": $(go.Link,
                                        {
                                            curve: go.Link.Bezier,
                                            fromShortLength: -2,
                                            toShortLength: -2,
                                            selectable: false
                                        },
                                $(go.Shape,
                                        {strokeWidth: 3},
                                new go.Binding("stroke", "toNode", function (n) {
                                    if (n.data.brush)
                                        return n.data.brush;
                                    return "black";
                                }).ofObject())
                                        ),
                                "contextMenu": $(go.Adornment, "Vertical",
                                        $("ContextMenuButton",
                                                $(go.TextBlock, "Undo"),
                                                {click: function (e, obj) {
                                                        e.diagram.commandHandler.undo();
                                                    }},
                                        new go.Binding("visible", "", function (o) {
                                            return o.diagram.commandHandler.canUndo();
                                        }).ofObject()),
                                        $("ContextMenuButton",
                                                $(go.TextBlock, "Redo"),
                                                {click: function (e, obj) {
                                                        e.diagram.commandHandler.redo();
                                                    }},
                                        new go.Binding("visible", "", function (o) {
                                            return o.diagram.commandHandler.canRedo();
                                        }).ofObject()),
                                        $("ContextMenuButton",
                                                $(go.TextBlock, "Save"),
                                                {click: function (e, obj) {
                                                        save();
                                                    }})),
                                // when the user drags a node, also move/copy/delete the whole subtree starting with that node
                                "commandHandler.copiesTree": true,
                                "commandHandler.deletesTree": true,
                                "draggingTool.dragsTree": true,
                                initialContentAlignment: go.Spot.Center,
                                "undoManager.isEnabled": true
                            });
            // whenever a GoJS transaction has finished modifying the model, update all Angular bindings
            function updateAngular(e) {
                if (e.isTransactionFinished)
                    scope.$apply();
            }
            // notice when the value of "model" changes: update the Diagram.model
            scope.$watch("model", function (newmodel) {
                var oldmodel = diagram.model;
                if (oldmodel !== newmodel) {
                    if (oldmodel)
                        oldmodel.removeChangedListener(updateAngular);
                    newmodel.addChangedListener(updateAngular);
                    diagram.model = newmodel;
                }
            });
            scope.$watch("model.selectedNodeData.name", function (newname) {
                // disable recursive updates
                diagram.model.removeChangedListener(updateAngular);
                // change the name
                diagram.startTransaction("change name");
                // the data property has already been modified, so setDataProperty would have no effect
                var node = diagram.findNodeForData(diagram.model.selectedNodeData);
                if (node !== null)
                    node.updateTargetBindings("name");
                diagram.commitTransaction("change name");
                // re-enable normal updates
                diagram.model.addChangedListener(updateAngular);
            });
            // update the model when the selection changes
            diagram.addDiagramListener("Modified", function (e) {
                var button = document.getElementById("SaveButton");
                if (button)
                    button.disabled = !diagram.isModified;
                var idx = document.title.indexOf("*");
                if (diagram.isModified) {
                    if (idx < 0)
                        document.title += "*";
                } else {
                    if (idx >= 0)
                        document.title = document.title.substr(0, idx);
                }
            });
            function spotConverter(dir, from) {
                if (dir === "left") {
                    return (from ? go.Spot.Left : go.Spot.Right);
                } else {
                    return (from ? go.Spot.Right : go.Spot.Left);
                }
            }

            function changeTextSize(obj, factor) {
                var adorn = obj.part;
                adorn.diagram.startTransaction("Change Text Size");
                var node = adorn.adornedPart;
                var tb = node.findObject("TEXT");
                tb.scale *= factor;
                adorn.diagram.commitTransaction("Change Text Size");
            }

            function toggleTextWeight(obj) {
                var adorn = obj.part;
                adorn.diagram.startTransaction("Change Text Weight");
                var node = adorn.adornedPart;
                var tb = node.findObject("TEXT");
                // assume "bold" is at the start of the font specifier
                var idx = tb.font.indexOf("bold");
                if (idx < 0) {
                    tb.font = "bold " + tb.font;
                } else {
                    tb.font = tb.font.substr(idx + 5);
                }
                adorn.diagram.commitTransaction("Change Text Weight");
            }

            function addNodeAndLink(e, obj) {
                var adorn = obj.part;
                var diagram = adorn.diagram;
                diagram.startTransaction("Add Node");
                var oldnode = adorn.adornedPart;
                var olddata = oldnode.data;
                // copy the brush and direction to the new node data
                var newdata = {text: "idea", brush: olddata.brush, dir: olddata.dir, parent: olddata.key};
                diagram.model.addNodeData(newdata);
                layoutTree(oldnode);
                diagram.commitTransaction("Add Node");
            }

            function layoutTree(node) {
                if (node.data.key === 0) {  // adding to the root?
                    layoutAll(); // lay out everything
                } else {  // otherwise lay out only the subtree starting at this parent node
                    var parts = node.findTreeParts();
                    layoutAngle(parts, node.data.dir === "left" ? 180 : 0);
                }
            }

            function layoutAngle(parts, angle) {
                var layout = go.GraphObject.make(go.TreeLayout,
                        {angle: angle,
                            arrangement: go.TreeLayout.ArrangementFixedRoots,
                            nodeSpacing: 5,
                            layerSpacing: 20});
                layout.doLayout(parts);
            }

            function layoutAll() {
                var root = diagram.findNodeForKey(0);
                if (root === null)
                    return;
                diagram.startTransaction("Layout");
                // split the nodes and links into two collections
                var rightward = new go.Set(go.Part);
                var leftward = new go.Set(go.Part);
                root.findLinksConnected().each(function (link) {
                    var child = link.toNode;
                    if (child.data.dir === "left") {
                        leftward.add(root); // the root node is in both collections
                        leftward.add(link);
                        leftward.addAll(child.findTreeParts());
                    } else {
                        rightward.add(root); // the root node is in both collections
                        rightward.add(link);
                        rightward.addAll(child.findTreeParts());
                    }
                });
                // do one layout and then the other without moving the shared root node
                layoutAngle(rightward, 0);
                layoutAngle(leftward, 180);
                diagram.commitTransaction("Layout");
            }

            // Show the diagram's model in JSON format
            function save() {
                document.getElementById("mySavedModel").value = diagram.model.toJson();
                diagram.isModified = false;
            }
        }
    };
});
app.directive('goDiagramState', function () {
    return {
        restrict: 'E',
        template: '<div></div>', // just an empty DIV element
        replace: true,
        scope: {model: '=goModel'},
        link: function (scope, element, attrs) {
            var $ = go.GraphObject.make;
            var diagram = // create a Diagram for the given HTML DIV element
                    $(go.Diagram, element[0],
                            {
                                "linkTemplate": $(go.Link, // the whole link panel
                                        {curve: go.Link.Bezier, adjusting: go.Link.Stretch, reshapable: true},
                                new go.Binding("curviness", "curviness"),
                                        new go.Binding("points").makeTwoWay(),
                                        $(go.Shape, // the link shape
                                                {strokeWidth: 1.5}),
                                        $(go.Shape, // the arrowhead
                                                {toArrow: "standard", stroke: null}),
                                        $(go.Panel, "Auto",
                                                $(go.Shape, // the link shape
                                                        {
                                                            fill: $(go.Brush, "Radial",
                                                                    {0: "rgb(240, 240, 240)", 0.3: "rgb(240, 240, 240)", 1: "rgba(240, 240, 240, 0)"}),
                                                            stroke: null
                                                        }),
                                                $(go.TextBlock, "transition", // the label
                                                        {
                                                            textAlign: "center",
                                                            font: "10pt helvetica, arial, sans-serif",
                                                            stroke: "black",
                                                            margin: 4,
                                                            editable: true  // editing the text automatically updates the model data
                                                        },
                                                new go.Binding("text", "text").makeTwoWay())
                                                )
                                        ),
                                "nodeTemplate.selectionAdornmentTemplate":
                                        $(go.Adornment, "Spot",
                                                $(go.Panel, "Auto",
                                                        $(go.Shape, {fill: null, stroke: "blue", strokeWidth: 2}),
                                                        $(go.Placeholder)  // this represents the selected Node
                                                        ),
                                                // the button to create a "next" node, at the top-right corner
                                                $("Button",
                                                        {
                                                            alignment: go.Spot.TopRight,
                                                            click: addNodeAndLink  // this function is defined below
                                                        },
                                                $(go.Shape, "PlusLine", {desiredSize: new go.Size(6, 6)})
                                                        ) // end button
                                                ),
                                "nodeTemplate": $(go.Node, "Auto",
                                        new go.Binding("location", "loc", go.Point.parse).makeTwoWay(go.Point.stringify),
                                        // define the node's outer shape, which will surround the TextBlock
                                        $(go.Shape, "RoundedRectangle",
                                                {
                                                    parameter1: 20, // the corner has a large radius
                                                    fill: $(go.Brush, "Linear", {0: "rgb(173,216,230)", 1: "rgb(173,216,230)"}),
                                                    stroke: "black",
                                                    portId: "",
                                                    fromLinkable: true,
                                                    fromLinkableSelfNode: true,
                                                    fromLinkableDuplicates: true,
                                                    toLinkable: true,
                                                    toLinkableSelfNode: true,
                                                    toLinkableDuplicates: true,
                                                    cursor: "pointer"
                                                }),
                                        $(go.TextBlock,
                                                {
                                                    font: "bold 11pt helvetica, bold arial, sans-serif",
                                                    editable: true  // editing the text automatically updates the model data
                                                },
                                        new go.Binding("text", "text").makeTwoWay())
                                        ),
                                // start everything in the middle of the viewport
                                initialContentAlignment: go.Spot.Center,
                                // have mouse wheel events zoom in and out instead of scroll up and down
                                "toolManager.mouseWheelBehavior": go.ToolManager.WheelZoom,
                                // support double-click in background creating a new node
                                "clickCreatingTool.archetypeNodeData": {text: "new node"},
                                // enable undo & redo
                                "undoManager.isEnabled": true
                            });
            // whenever a GoJS transaction has finished modifying the model, update all Angular bindings
            function updateAngular(e) {
                if (e.isTransactionFinished)
                    scope.$apply();
            }
             // notice when the value of "model" changes: update the Diagram.model
            scope.$watch("model", function (newmodel) {
                var oldmodel = diagram.model;
                if (oldmodel !== newmodel) {
                    if (oldmodel)
                        oldmodel.removeChangedListener(updateAngular);
                    newmodel.addChangedListener(updateAngular);
                    diagram.model = newmodel;
                }
            });
            scope.$watch("model.selectedNodeData.name", function (newname) {
                // disable recursive updates
                diagram.model.removeChangedListener(updateAngular);
                // change the name
                diagram.startTransaction("change name");
                // the data property has already been modified, so setDataProperty would have no effect
                var node = diagram.findNodeForData(diagram.model.selectedNodeData);
                if (node !== null)
                    node.updateTargetBindings("name");
                diagram.commitTransaction("change name");
                // re-enable normal updates
                diagram.model.addChangedListener(updateAngular);
            });
            
            diagram.addDiagramListener("Modified", function (e) {
                var button = document.getElementById("SaveButton");
                if (button)
                    button.disabled = !diagram.isModified;
                var idx = document.title.indexOf("*");
                if (diagram.isModified) {
                    if (idx < 0)
                        document.title += "*";
                } else {
                    if (idx >= 0)
                        document.title = document.title.substr(0, idx);
                }
            });
            // clicking the button inserts a new node to the right of the selected node,
            // and adds a link to that new node
            function addNodeAndLink(e, obj) {
                var adorn = obj.part;
                e.handled = true;
                var diagram = adorn.diagram;
                diagram.startTransaction("Add State");
                // get the node data for which the user clicked the button
                var fromNode = adorn.adornedPart;
                var fromData = fromNode.data;
                // create a new "State" data object, positioned off to the right of the adorned Node
                var toData = {text: "new"};
                var p = fromNode.location.copy();
                p.x += 200;
                toData.loc = go.Point.stringify(p); // the "loc" property is a string, not a Point object
                // add the new node data to the model
                var model = diagram.model;
                model.addNodeData(toData);
                // create a link data from the old node data to the new node data
                var linkdata = {
                    from: model.getKeyForNodeData(fromData), // or just: fromData.id
                    to: model.getKeyForNodeData(toData),
                    text: "transition"
                };
                // and add the link data to the model
                model.addLinkData(linkdata);
                // select the new Node
                var newnode = diagram.findNodeForData(toData);
                diagram.select(newnode);
                diagram.commitTransaction("Add State");
                // if the new node is off-screen, scroll the diagram to show the new node
                diagram.scrollToRect(newnode.actualBounds);
            }


            // Show the diagram's model in JSON format
            function save() {
                console.log(diagram.model.toJson());
                diagram.isModified = false;
            }
            function load() {
                diagram.model = go.Model.fromJson(document.getElementById("mySavedModel").value);
            }
        }
    };
});



