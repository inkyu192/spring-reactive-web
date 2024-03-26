package spring.reactive.web.java.common;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.ByteBuffer;
import java.util.List;

//@RequiredArgsConstructor
public class S3UploadService {

//    private final S3AsyncClient s3AsyncClient;
//
//    private Mono<String> putObjectSingle(String bucket, String key, FilePart filePart, Long fileSize) {
//        return filePart.content()
//                .flatMapSequential(dataBuffer -> Flux.fromIterable(dataBuffer::readableByteBuffers))
//                .collectList()
//                .flatMap(byteBuffers -> Mono.fromFuture(
//                        s3AsyncClient.putObject(
//                                PutObjectRequest.builder()
//                                        .bucket(bucket)
//                                        .key(key)
//                                        .contentLength(fileSize)
//                                        .build(),
//                                AsyncRequestBody.fromPublisher(Flux.fromIterable(byteBuffers))
//                        )
//                ))
//                .thenReturn(key);
//    }
//
//    private Mono<String> putObjectMultipart(String bucket, String key, FilePart filePart) {
//        UploadStatus uploadStatus = new UploadStatus(bucket, key);
//
//        return createMultipartUpload(uploadStatus)
//                .flatMap(createMultipartUploadResponse -> {
//                    String uploadId = createMultipartUploadResponse.uploadId();
//
//                    uploadStatus.setUploadId(uploadId);
//
//                    return filePart.content()
//                            .bufferUntil(dataBuffer -> {
//                                uploadStatus.addBuffered(dataBuffer.readableByteCount());
//
//                                if (uploadStatus.getBuffered() >= 5242880) {
//                                    uploadStatus.setBuffered(0);
//                                    return true;
//                                }
//
//                                return false;
//                            })
//                            .map(dataBuffers -> {
//                                ByteBuffer byteBuffer = ByteBuffer.allocate(
//                                        dataBuffers.stream()
//                                                .mapToInt(DataBuffer::readableByteCount)
//                                                .sum());
//
//                                for (DataBuffer dataBuffer : dataBuffers) {
//                                    byteBuffer.put(dataBuffer.asByteBuffer());
//                                }
//
//                                byteBuffer.rewind();
//
//                                return byteBuffer;
//                            })
//                            .flatMapSequential(byteBuffer -> uploadPart(uploadStatus, byteBuffer))
//                            .collectList()
//                            .flatMap(completedParts -> completeMultipartUpload(uploadStatus, completedParts))
//                            .thenReturn(key);
//                });
//    }
//
//    private Mono<CreateMultipartUploadResponse> createMultipartUpload(UploadStatus uploadStatus) {
//        return Mono.fromFuture(
//                s3AsyncClient.createMultipartUpload(
//                        CreateMultipartUploadRequest.builder()
//                                .bucket(uploadStatus.getBucket())
//                                .key(uploadStatus.getKey())
//                                .build()
//                )
//        );
//    }
//
//    private Mono<CompletedPart> uploadPart(UploadStatus uploadStatus, ByteBuffer byteBuffer) {
//        int partNumber = uploadStatus.getPartNumber();
//
//        return Mono.fromFuture(
//                        s3AsyncClient.uploadPart(
//                                UploadPartRequest.builder()
//                                        .bucket(uploadStatus.getBucket())
//                                        .key(uploadStatus.getKey())
//                                        .uploadId(uploadStatus.getUploadId())
//                                        .partNumber(partNumber)
//                                        .contentLength((long) byteBuffer.remaining())
//                                        .build(),
//                                AsyncRequestBody.fromByteBuffer(byteBuffer)
//                        )
//                )
//                .map(uploadPartResponse -> CompletedPart.builder()
//                        .partNumber(partNumber)
//                        .eTag(uploadPartResponse.eTag())
//                        .build());
//    }
//
//    private Mono<CompleteMultipartUploadResponse> completeMultipartUpload(
//            UploadStatus uploadStatus,
//            List<CompletedPart> completedParts
//    ) {
//        CompletedMultipartUpload multipartUpload = CompletedMultipartUpload.builder()
//                .parts(completedParts)
//                .build();
//
//        return Mono.fromFuture(
//                s3AsyncClient.completeMultipartUpload(
//                        CompleteMultipartUploadRequest.builder()
//                                .bucket(uploadStatus.getBucket())
//                                .key(uploadStatus.getKey())
//                                .uploadId(uploadStatus.getUploadId())
//                                .multipartUpload(multipartUpload)
//                                .build()
//                )
//        );
//    }
//
//    public Mono<Void> deleteObject(String bucket, String key) {
//        return Mono.fromFuture(
//                        s3AsyncClient.deleteObject(
//                                DeleteObjectRequest.builder()
//                                        .bucket(bucket)
//                                        .key(key)
//                                        .build()
//                        )
//                )
//                .then();
//    }
}
