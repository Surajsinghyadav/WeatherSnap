package com.suraj.weathersnap.Presentation

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.suraj.weathersnap.data.local.CameraState
import com.suraj.weathersnap.ui.theme.HeaderGreenLight
import com.suraj.weathersnap.util.compressImage
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.Executors

@Composable
fun CameraScreen(
    onPhotoCaptured: (imagePath: String, originalKb: Int, compressedKb: Int) -> Unit,
    onClose: () -> Unit
) {
    val context = LocalContext.current

    // Check if permission is already granted
    var hasPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context, Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    // Permission launcher — shows the system permission dialog
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasPermission = isGranted
    }

    // Ask for permission as soon as screen opens
    LaunchedEffect(Unit) {
        if (!hasPermission) {
            permissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color.Black)) {

        if (hasPermission) {
            // ── Show camera only after permission granted ──
            CameraContent(
                onPhotoCaptured = onPhotoCaptured,
                onClose = onClose
            )
        } else {
            // ── Show permission denied UI ──
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "Camera Permission Required",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(12.dp))
                Text(
                    "Please allow camera access to capture photos for your report.",
                    color = Color.White.copy(0.6f),
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(24.dp))
                // Allow user to retry permission
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(30.dp))
                        .background(HeaderGreenLight)
                        .clickable { permissionLauncher.launch(Manifest.permission.CAMERA) }
                        .padding(horizontal = 32.dp, vertical = 12.dp)
                ) {
                    Text(
                        "Grant Permission",
                        color = Color(0xFF1A2205),
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(Modifier.height(16.dp))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(30.dp))
                        .border(1.dp, Color.White.copy(0.4f), RoundedCornerShape(30.dp))
                        .clickable { onClose() }
                        .padding(horizontal = 32.dp, vertical = 12.dp)
                ) {
                    Text("Go Back", color = Color.White)
                }
            }
        }
    }
}

// ── Actual Camera UI — only shown when permission is granted ─────

@Composable
private fun CameraContent(
    onPhotoCaptured: (imagePath: String, originalKb: Int, compressedKb: Int) -> Unit,
    onClose: () -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val executor = remember { Executors.newSingleThreadExecutor() }
    val imageCapture = remember { ImageCapture.Builder().build() }

    DisposableEffect(Unit) {
        onDispose { executor.shutdown() }
    }

    Box(modifier = Modifier.fillMaxSize()) {

        // Live Preview
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { ctx ->
                PreviewView(ctx).apply {
                    scaleType = PreviewView.ScaleType.FILL_CENTER
                }
            },
            update = { previewView ->
                val future = ProcessCameraProvider.getInstance(context)
                future.addListener({
                    val cameraProvider = future.get()
                    val preview = Preview.Builder().build().also {
                        it.setSurfaceProvider(previewView.surfaceProvider)
                    }
                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(
                        lifecycleOwner,
                        CameraSelector.DEFAULT_BACK_CAMERA,
                        preview,
                        imageCapture
                    )
                }, ContextCompat.getMainExecutor(context))
            }
        )

        // Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopStart)
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Custom Camera",
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .border(1.dp, Color.White.copy(0.5f), RoundedCornerShape(20.dp))
                    .clickable { onClose() }
                    .padding(horizontal = 20.dp, vertical = 8.dp)
            ) {
                Text("Close", color = Color.White, fontSize = 13.sp)
            }
        }

        // Capture Button
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .padding(horizontal = 32.dp, vertical = 32.dp)
        ) {
            var cameraState by remember { mutableStateOf(CameraState.IDEAL) }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(30.dp))
                    .background(HeaderGreenLight)
                    .clickable {
                        cameraState = CameraState.PROCESSING
                        takePhoto(
                            context = context,
                            imageCapture = imageCapture,
                            executor = executor,
                            onDone = { file ->
                                val result = compressImage(context, file, onDone = onClose)
                                onPhotoCaptured(
                                    result.compressedFile.absolutePath,
                                    result.originalSizeKb,
                                    result.compressedSizeKb
                                )
                            }
                        );
                    }
                    .padding(vertical = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                when (cameraState) {
                    CameraState.IDEAL -> {
                        Text(
                            "Capture",
                            color = Color(0xFF1A2205),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    CameraState.PROCESSING -> {
                        CircularProgressIndicator(
                            color = Color.Black
                        )

                    }
                }
            }
        }
    }
}

private fun takePhoto(
    context: Context,
    imageCapture: ImageCapture,
    executor: java.util.concurrent.ExecutorService,
    onDone: (File) -> Unit
) {
    val file = File(
        context.cacheDir,
        "IMG_${
            SimpleDateFormat(
                "yyyyMMdd_HHmmss",
                Locale.US
            ).format(System.currentTimeMillis())
        }.jpg"
    )
    val options = ImageCapture.OutputFileOptions.Builder(file).build()
    imageCapture.takePicture(
        options, executor,
        object : ImageCapture.OnImageSavedCallback {
            override fun onImageSaved(output: ImageCapture.OutputFileResults) = onDone(file)
            override fun onError(e: ImageCaptureException) {}
        }
    )
}