﻿<?php
    $path_htdocs = '/Applications/XAMPP/xamppfiles/htdocs';

    $querysubmitted = FALSE;

    $queryresult = '';

    //var_dump($_POST);
    if ($_SERVER["REQUEST_METHOD"] == "POST") {
        $queryresult = '';

        if (($_POST['dataset'] == "forum") && ($_POST['queries_forum'] == "Q1")) {
           $queryFileName = "query01.txt";
        } else if (($_POST['dataset'] == "forum") && ($_POST['queries_forum'] == "Q2")) {
           $queryFileName = "query02.txt";
        } else if (($_POST['dataset'] == "synthetic") && ($_POST['queries_synthetic'] == "Q1")) {
           $queryFileName = "query11.txt";
        } else if (($_POST['dataset'] == "synthetic") && ($_POST['queries_synthetic'] == "Q2")) {
           $queryFileName = "query12.txt";
        } else if (($_POST['dataset'] == "synthetic") && ($_POST['queries_synthetic'] == "Q3")) {
           $queryFileName = "query13.txt";
        } else if (($_POST['dataset'] == "synthetic") && ($_POST['queries_synthetic'] == "Q4")) {
           $queryFileName = "query14.txt";
        } else if (($_POST['dataset'] == "synthetic") && ($_POST['queries_synthetic'] == "Q5")) {
           $queryFileName = "query15.txt";
        } else if (($_POST['dataset'] == "synthetic") && ($_POST['queries_synthetic'] == "Q6")) {
           $queryFileName = "query16.txt";
        } else if (($_POST['dataset'] == "synthetic") && ($_POST['queries_synthetic'] == "Q7")) {
           $queryFileName = "query17.txt";
        } else if (($_POST['dataset'] == "synthetic") && ($_POST['queries_synthetic'] == "Q8")) {
           $queryFileName = "query18.txt";
        } else if ($_POST['dataset'] == "custom") {
           $queryFileName = "custom.txt";
           $query_text = ($_POST['query_text']);
           $query_file = fopen("/Applications/XAMPP/xamppfiles/htdocs/paxquery/io/" . $queryFileName, "w") or die("Unable to open file!");
           fwrite($query_file, $query_text);
           fclose($query_file);
        }

        $queryresult = shell_exec('export DYLD_LIBRARY_PATH=\"\"; /Applications/XAMPP/xamppfiles/htdocs/paxquery/paxquery-installation/scripts/paxquery-run.sh /Applications/XAMPP/xamppfiles/htdocs/paxquery/io/' . $queryFileName . ' /Applications/XAMPP/xamppfiles/htdocs/paxquery/io/xoutput.xml 1 drawtrees');
        print $queryFileName;
        $querysubmitted = TRUE;
       
        /* replace ampersands */
        shell_exec("sed -f /Applications/XAMPP/xamppfiles/htdocs/paxquery/utils/sed-replace-amp.sed /Applications/XAMPP/xamppfiles/htdocs/paxquery/io/xoutput.xml > /Applications/XAMPP/xamppfiles/htdocs/paxquery/io/xoutput2.xml");
        /* wrap result with <output></output> */
        shell_exec('echo \'<output>\' > /Applications/XAMPP/xamppfiles/htdocs/paxquery/io/final.xml');
        shell_exec('less /Applications/XAMPP/xamppfiles/htdocs/paxquery/io/xoutput2.xml >> /Applications/XAMPP/xamppfiles/htdocs/paxquery/io/final.xml');
        shell_exec('echo \'</output>\' >> /Applications/XAMPP/xamppfiles/htdocs/paxquery/io/final.xml');

    }
?>

<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="description" content="PAXQuery demo">
<title>PAXQuery demo</title>
<link rel="stylesheet" href="css/pure-min.css">
<link rel="stylesheet" href="css/layouts/side-menu.css">
<link rel="stylesheet" href="css/custom.css">
</head>
<body>
<div id="layout">
    <!-- Menu toggle -->
    <a href="#menu" id="menuLink" class="menu-link">
        <!-- Hamburger icon -->
        <span></span>
    </a>

    <div id="menu">
        <div class="pure-menu pure-menu-open">
            <a class="pure-menu-heading" href="#">PAXQuery</a>

            <ul>
                <li><a href="datasets.php">Datasets</a></li>
                <li class="menu-item-divided pure-menu-selected">
                    <a href="index.php">Queries</a></li>
                <li><a href="tps.php">Tree Patterns</a></li>
                <li><a href="algebra.php">Logical Plan</a></li>
                <li><a href="pact.php">Flink Input</a></li>
                <li><a href="output.php">Output</a></li>
            </ul>
        </div>
    </div>

    <div id="main">
        <div class="header">
            <h1>Queries</h1>
            <!--<h2>A subtitle for your page goes here</h2>-->
        </div>

        <div class="content">
            <form action="<?php echo $_SERVER['PHP_SELF']; ?>" method="post" class="pure-form pure-form-stacked">
<fieldset>
<!--    <div class="form-group">-->
      <label for="dataset">Dataset</label>
      <select class="form-control" id="dataset" name="dataset" style="width: 100%">
        <option value="forum" <?= ($querysubmitted && $_POST['dataset'] == "forum") ? 'selected' : '' ?>>Forum</option>
        <option value="synthetic" <?= ($querysubmitted && $_POST['dataset'] == "synthetic") ? 'selected' : '' ?>>Synthetic</option>
        <option value="custom" <?= ($querysubmitted && $_POST['dataset'] == "custom") ? 'selected' : '' ?>>Custom</option>
      </select>
    <!--</div>-->
    <div class="form-group">
      <label for="query">Query</label>
      <select class="form-control" id="queries_forum" name="queries_forum" style="width: 100%">
        <option value="NULL">Forum predefined queries</option>
        <option value="Q1" <?= ($querysubmitted && $_POST['dataset'] == "forum") && ($_POST['queries_forum'] == "Q1") ? 'selected' : '' ?>>Q1 - Return all posts from user "LWaB"</option>
        <option value="Q2" <?= ($querysubmitted && $_POST['dataset'] == "forum") && ($_POST['queries_forum'] == "Q2") ? 'selected' : '' ?>>Q2 - Return all users with more than 100 posts in historical</option>
      </select>
      <select class="form-control" id="queries_synthetic" name="queries_synthetic" style="width: 100%">
        <option value="NULL">Synthetic predefined queries</option>
        <option value="Q1" <?= ($querysubmitted && $_POST['dataset'] == "synthetic") && ($_POST['queries_synthetic'] == "Q1") ? 'selected' : '' ?>>Q1 - Return all purchases and their corresponding item descriptions for which the buyer is older than 30</option>
        <option value="Q2" <?= ($querysubmitted && $_POST['dataset'] == "synthetic") && ($_POST['queries_synthetic'] == "Q2") ? 'selected' : '' ?>>Q2 - Return the number of purchases (if any) and their corresponding item references for every buyer</option>
        <option value="Q3" <?= ($querysubmitted && $_POST['dataset'] == "synthetic") && ($_POST['queries_synthetic'] == "Q3") ? 'selected' : '' ?>>Q3 - Return the name of the person with id "person0"</option>
        <option value="Q4" <?= ($querysubmitted && $_POST['dataset'] == "synthetic") && ($_POST['queries_synthetic'] == "Q4") ? 'selected' : '' ?>>Q4 - Return the names of all items whose description contains the word "gold"</option>
        <option value="Q5" <?= ($querysubmitted && $_POST['dataset'] == "synthetic") && ($_POST['queries_synthetic'] == "Q5") ? 'selected' : '' ?>>Q5 - How many sold items cost more than 40?</option>
        <option value="Q6" <?= ($querysubmitted && $_POST['dataset'] == "synthetic") && ($_POST['queries_synthetic'] == "Q6") ? 'selected' : '' ?>>Q6 - List all persons according to their interest</option>
        <option value="Q7" <?= ($querysubmitted && $_POST['dataset'] == "synthetic") && ($_POST['queries_synthetic'] == "Q7") ? 'selected' : '' ?>>Q7 - List the names of persons and the number of items they bought</option>
        <option value="Q8" <?= ($querysubmitted && $_POST['dataset'] == "synthetic") && ($_POST['queries_synthetic'] == "Q8") ? 'selected' : '' ?>>Q8 - List the name of users in United States and the items that they bought or sold in an auction</option>
      </select>
      <select class="form-control" id="queries_custom" name="queries_custom" style="width: 100%">
        <option value="NULL">Enter your query</option>
      </select>
            <p><label id="debug" name="debug"><?php echo $query_text?></label></p>
      <p><div class="queryfield" id="query" contenteditable="false" style="<?php echo ($_POST['query_text']=='') ? 'width: 100%; height: 320px; display: block' : 'width: 100%; height: 320px; display: none' ?>"></div></p>
      <p><textarea class="queryfield" id="query_text" name="query_text" style="<?php echo ($_POST['query_text']=='') ? 'width: 100%; height: 320px; display: none' : 'width: 100%; height: 320px; display: inline' ?>"><?php echo isset($_POST['query_text']) ? $_POST['query_text'] : ''; ?></textarea><p>
    </div>
    <p><input id="submit" name="submit1" type="submit" value="Submit" class="pure-button pure-button-primary" style="float: right"><p>
    <br>
    <p><label>Execution output</label></p>
<p><textarea readonly="true" style="width: 100%" rows="<?php if($querysubmitted) print 10; else print 5 ?>"><?php if($querysubmitted) print $queryresult?></textarea></p>

<!--<p><div class="queryfield" style="width: 100%;"><xqreserved>fdsa</xqreserved>fdsa</div></p>-->
</fieldset>


</div>
</div>
</div>





<script src="js/ui.js"></script>
<script src="js/jquery-2.1.1.min.js"></script>
<script src="js/queries.js"></script>
<script>
    $(document).ready(function () {
      <?php 
      if ($querysubmitted)
        echo "document.getElementById('query').innerHTML = ". (($_POST['dataset'] == "forum") ? "queries_forum['" . $_POST['queries_forum'] : "queries_synthetic['" . $_POST['queries_synthetic']) ."'];\n";

      ?>
      $("#queries_forum").<?= (!$querysubmitted || ($_POST['dataset'] == "forum")) ? 'show' : 'hide' ?>();
      $("#queries_synthetic").<?= ($querysubmitted && ($_POST['dataset'] == "synthetic")) ? 'show' : 'hide' ?>();
      $("#queries_custom").<?= ($querysubmitted && ($_POST['dataset'] == "custom")) ? 'show' : 'hide' ?>(); 

    });

    $("#dataset").change(function () {
    $("#query").val("");    
    switch($("#dataset").val()) {
    case "forum":
        $("#queries_forum").show();
        $("#queries_forum").prop("selectedIndex", 0);
        $("#queries_synthetic").hide();
        $("#queries_custom").hide();
        break;
    case "synthetic":
        $("#queries_forum").hide();
        $("#queries_synthetic").show();
        $("#queries_synthetic").prop("selectedIndex", 0);
        $("#queries_custom").hide();
        break;
    case "custom":
        $("#queries_forum").hide();
        $("#queries_synthetic").hide();
        $("#queries_custom").show();
        $("#queries_custom").prop("selectedIndex", 0);
        break;

    }
    });
                                  
    $("#queries_forum").change(function () {
    //if($("#queries_forum") > 0) {
      //$("#query").val(queries_forum[$("#queries_forum").val()]);
      //$("#query").text(queries_forum[$("#queries_forum").val()]);
      document.getElementById('query').style.display = 'block';
      document.getElementById('query_text').style.display = 'none';
      document.getElementById('query').innerHTML = queries_forum[$("#queries_forum").val()];
    });
    $("#queries_synthetic").change(function () {
    //else if($("#queries_synthetic") > 0) {                                           
      //$("#query").val(queries_synthetic[$("#queries_synthetic").val()]);
      //$("#query").text(queries_synthetic[$("#queries_synthetic").val()]);
      document.getElementById('query').style.display = 'block';
      document.getElementById('query_text').style.display = 'none';
      document.getElementById('query').innerHTML = queries_synthetic[$("#queries_synthetic").val()];
    });
    $("#dataset").change(function () {
    //else if($("#queries_synthetic") > 0) {                                           
      //$("#query").val(queries_synthetic[$("#queries_synthetic").val()]);
      //$("#query").text(queries_synthetic[$("#queries_synthetic").val()]);
      if($("#dataset").val() == "custom" && $("#query").val() == "") {
        //document.getElementById('query').innerHTML = queries_synthetic[$("#queries_synthetic").val()];
        document.getElementById('query').style.display = 'none';
        document.getElementById('query_text').style.display = 'block';
        document.getElementById('query_text').style.width = '100%';
        document.getElementById('query_text').innerHTML = "";
      }
    });
    </script>


</body>
</html>
